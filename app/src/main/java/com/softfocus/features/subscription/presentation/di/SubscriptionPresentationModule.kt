package com.softfocus.features.subscription.presentation.di

import android.content.Context
import com.softfocus.features.subscription.data.di.SubscriptionDataModule
import com.softfocus.features.subscription.domain.usecases.CancelSubscriptionUseCase
import com.softfocus.features.subscription.domain.usecases.CreateCheckoutSessionUseCase
import com.softfocus.features.subscription.domain.usecases.GetMySubscriptionUseCase
import com.softfocus.features.subscription.domain.usecases.GetUsageStatsUseCase
import com.softfocus.features.subscription.domain.usecases.HandleCheckoutSuccessUseCase
import com.softfocus.features.subscription.domain.usecases.InitializeSubscriptionUseCase

object SubscriptionPresentationModule {

    fun getGetMySubscriptionUseCase(context: Context): GetMySubscriptionUseCase {
        return GetMySubscriptionUseCase(
            SubscriptionDataModule.getSubscriptionRepository(context)
        )
    }

    fun getGetUsageStatsUseCase(context: Context): GetUsageStatsUseCase {
        return GetUsageStatsUseCase(
            SubscriptionDataModule.getSubscriptionRepository(context)
        )
    }

    fun getCreateCheckoutSessionUseCase(context: Context): CreateCheckoutSessionUseCase {
        return CreateCheckoutSessionUseCase(
            SubscriptionDataModule.getSubscriptionRepository(context)
        )
    }

    fun getCancelSubscriptionUseCase(context: Context): CancelSubscriptionUseCase {
        return CancelSubscriptionUseCase(
            SubscriptionDataModule.getSubscriptionRepository(context)
        )
    }

    fun getInitializeSubscriptionUseCase(context: Context): InitializeSubscriptionUseCase {
        return InitializeSubscriptionUseCase(
            SubscriptionDataModule.getSubscriptionRepository(context)
        )
    }

    fun getHandleCheckoutSuccessUseCase(context: Context): HandleCheckoutSuccessUseCase {
        return HandleCheckoutSuccessUseCase(
            SubscriptionDataModule.getSubscriptionRepository(context)
        )
    }

}
