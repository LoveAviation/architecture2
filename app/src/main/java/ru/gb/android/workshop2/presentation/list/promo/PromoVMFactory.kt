package ru.gb.android.workshop2.presentation.list.promo

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import ru.gb.android.workshop2.domain.promo.ConsumePromosUseCase

@Suppress("UNCHECKED_CAST")
class PromoVMFactory(
    private val promoVOMapper: PromoVOMapper,
    private val consumePromosUseCase: ConsumePromosUseCase,
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(PromoVM::class.java)) {
            return PromoVM(
                promoVOMapper,
                consumePromosUseCase
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}