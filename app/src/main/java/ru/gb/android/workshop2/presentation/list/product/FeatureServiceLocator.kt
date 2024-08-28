package ru.gb.android.workshop2.presentation.list.product

import ru.gb.android.workshop2.ServiceLocator

object FeatureServiceLocator {

    private fun provideProductVOFactory(): ProductVOFactory {
        return ProductVOFactory(
            discountFormatter = ServiceLocator.provideDiscountFormatter(),
            priceFormatter = ServiceLocator.providePriceFormatter(),
        )
    }

    fun provideProductVMFactory(): ProductVMFactory{
        return ProductVMFactory(
            consumeProductsUseCase = ServiceLocator.provideConsumeProductsUseCase(),
            productVOFactory = provideProductVOFactory(),
            consumePromosUseCase = ServiceLocator.provideConsumePromosUseCase()
        )
    }
}