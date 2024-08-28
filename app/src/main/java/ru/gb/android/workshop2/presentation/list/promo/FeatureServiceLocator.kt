package ru.gb.android.workshop2.presentation.list.promo

import ru.gb.android.workshop2.ServiceLocator

object FeatureServiceLocator {

    private fun provideProductVOMapper(): PromoVOMapper {
        return PromoVOMapper()
    }

    fun providePromoVMFactory(): PromoVMFactory {
        return PromoVMFactory(
            promoVOMapper = provideProductVOMapper(),
            consumePromosUseCase = ServiceLocator.provideConsumePromosUseCase()
        )
    }
}