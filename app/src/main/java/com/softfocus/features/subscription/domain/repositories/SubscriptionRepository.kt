package com.softfocus.features.subscription.domain.repositories

import com.softfocus.features.subscription.domain.models.CheckoutSession
import com.softfocus.features.subscription.domain.models.FeatureAccess
import com.softfocus.features.subscription.domain.models.FeatureType
import com.softfocus.features.subscription.domain.models.Subscription
import com.softfocus.features.subscription.domain.models.UsageStats

interface SubscriptionRepository {
    suspend fun getMySubscription(): Result<Subscription>
    suspend fun getUsageStats(): Result<UsageStats>
    suspend fun checkFeatureAccess(featureType: FeatureType): Result<FeatureAccess>
    suspend fun createCheckoutSession(successUrl: String, cancelUrl: String): Result<CheckoutSession>
    suspend fun cancelSubscription(cancelImmediately: Boolean): Result<Subscription>
    suspend fun trackUsage(userId: String, featureType: FeatureType): Result<Unit>
    suspend fun initializeSubscription(): Result<Subscription>
    suspend fun handleCheckoutSuccess(sessionId: String): Result<Subscription>
}
