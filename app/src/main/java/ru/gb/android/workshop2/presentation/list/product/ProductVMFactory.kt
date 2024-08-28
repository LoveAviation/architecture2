package ru.gb.android.workshop2.presentation.list.product

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import ru.gb.android.workshop2.domain.product.ConsumeProductsUseCase
import ru.gb.android.workshop2.domain.promo.ConsumePromosUseCase

@Suppress("UNCHECKED_CAST")
class ProductVMFactory(
    private val consumeProductsUseCase: ConsumeProductsUseCase,
    private val productVOFactory: ProductVOFactory,
    private val consumePromosUseCase: ConsumePromosUseCase
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ProductVM::class.java)) {
            return ProductVM(
                consumeProductsUseCase,
                productVOFactory,
                consumePromosUseCase
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}