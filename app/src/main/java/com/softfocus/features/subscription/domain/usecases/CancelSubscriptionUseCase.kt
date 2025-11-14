package com.softfocus.features.subscription.domain.usecases

import com.softfocus.features.subscription.domain.models.Subscription
import com.softfocus.features.subscription.domain.repositories.SubscriptionRepository

class CancelSubscriptionUseCase(
    private val repository: SubscriptionRepository
) {
    suspend operator fun invoke(cancelImmediately: Boolean = false): Result<Subscription> {
        return repository.cancelSubscription(cancelImmediately)
    }
}
