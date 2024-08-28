package ru.gb.android.workshop2.presentation.list.promo

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import ru.gb.android.workshop2.domain.promo.ConsumePromosUseCase

class PromoVM(
    private val promoVOMapper: PromoVOMapper,
    private val consumePromosUseCase: ConsumePromosUseCase
): ViewModel() {

    private val _promo = MutableLiveData<List<PromoVO>?>()
    val promo: LiveData<List<PromoVO>?> = _promo


    private val _error = MutableStateFlow(false)
    val error: StateFlow<Boolean> = _error

    fun loadPromos() {
        consumePromosUseCase()
            .map { promos ->
                promos.map(promoVOMapper::map)
            }
            .onStart {
                _error.value = false
                _promo.value = null
            }
            .onEach { promoVOList ->
                _promo.value = promoVOList
            }
            .catch {
                _error.value = true
            }
            .launchIn(viewModelScope)
    }

    fun dispose() {
        viewModelScope.cancel()
    }

    fun refresh() {
        loadPromos()
    }
}