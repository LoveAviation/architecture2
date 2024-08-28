package ru.gb.android.workshop2.presentation.list.product

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import ru.gb.android.workshop2.domain.product.ConsumeProductsUseCase
import ru.gb.android.workshop2.domain.promo.ConsumePromosUseCase

class ProductVM(
    private val consumeProductsUseCase: ConsumeProductsUseCase,
    private val productVOFactory: ProductVOFactory,
    private val consumePromosUseCase: ConsumePromosUseCase
): ViewModel() {

    private val _product = MutableLiveData<List<ProductVO>?>()
    val product: LiveData<List<ProductVO>?> = _product

    private val _error = MutableStateFlow(false)
    val error: StateFlow<Boolean> = _error

    fun loadProduct() {
        combine(
            consumeProductsUseCase(),
            consumePromosUseCase(),
        ) { products, promos ->
            products.map { product -> productVOFactory.create(product, promos) }
        }
            .onStart {
                _error.value = false
                _product.value = null
            }
            .onEach { productListVO ->
                _product.value = productListVO
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
        loadProduct()
    }
}