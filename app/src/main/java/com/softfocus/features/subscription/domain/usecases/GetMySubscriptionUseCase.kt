package com.softfocus.features.subscription.domain.usecases

import com.softfocus.features.subscription.domain.models.Subscription
import com.softfocus.features.subscription.domain.repositories.SubscriptionRepository

class GetMySubscriptionUseCase(
    private val repository: SubscriptionRepository
) {
    suspend operator fun invoke(): Result<Subscription> {
        return repository.getMySubscription()
    }
}
