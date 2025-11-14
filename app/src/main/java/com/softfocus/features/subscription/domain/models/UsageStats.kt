package com.softfocus.features.subscription.domain.models

data class UsageStats(
    val plan: SubscriptionPlan,
    val featureUsages: List<FeatureUsage>,
    val generatedAt: String
)

data class FeatureUsage(
    val featureType: FeatureType,
    val currentUsage: Int,
    val limit: Int?,
    val isUnlimited: Boolean,
    val limitReached: Boolean,
    val remaining: Int?,
    val periodStart: String,
    val periodEnd: String,
    val lastUsedAt: String?
)

enum class FeatureType {
    AI_CHAT_MESSAGE,
    FACIAL_ANALYSIS,
    CONTENT_RECOMMENDATION,
    CHECK_IN,
    PATIENT_CONNECTION,
    CONTENT_ASSIGNMENT
}
