package com.softfocus.features.subscription.domain.usecases

import com.softfocus.features.subscription.domain.models.CheckoutSession
import com.softfocus.features.subscription.domain.repositories.SubscriptionRepository

class CreateCheckoutSessionUseCase(
    private val repository: SubscriptionRepository
) {
    suspend operator fun invoke(successUrl: String, cancelUrl: String): Result<CheckoutSession> {
        return repository.createCheckoutSession(successUrl, cancelUrl)
    }
}
