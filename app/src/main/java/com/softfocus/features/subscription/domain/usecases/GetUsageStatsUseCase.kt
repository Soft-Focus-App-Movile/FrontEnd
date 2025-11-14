package com.softfocus.features.subscription.domain.usecases

import com.softfocus.features.subscription.domain.models.UsageStats
import com.softfocus.features.subscription.domain.repositories.SubscriptionRepository

class GetUsageStatsUseCase(
    private val repository: SubscriptionRepository
) {
    suspend operator fun invoke(): Result<UsageStats> {
        return repository.getUsageStats()
    }
}
