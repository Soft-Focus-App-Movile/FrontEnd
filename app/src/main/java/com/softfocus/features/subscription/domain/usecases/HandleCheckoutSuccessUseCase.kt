package com.softfocus.features.subscription.domain.usecases

import com.softfocus.features.subscription.domain.models.Subscription
import com.softfocus.features.subscription.domain.repositories.SubscriptionRepository

class HandleCheckoutSuccessUseCase(
    private val repository: SubscriptionRepository
) {
    suspend operator fun invoke(sessionId: String): Result<Subscription> {
        return repository.handleCheckoutSuccess(sessionId)
    }
}
