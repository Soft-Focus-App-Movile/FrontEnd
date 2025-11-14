package com.softfocus.features.subscription.domain.models

data class Subscription(
    val id: String,
    val userId: String,
    val userType: String,
    val plan: SubscriptionPlan,
    val status: SubscriptionStatus,
    val currentPeriodStart: String?,
    val currentPeriodEnd: String?,
    val cancelAtPeriodEnd: Boolean,
    val cancelledAt: String?,
    val isActive: Boolean,
    val usageLimits: UsageLimits,
    val createdAt: String,
    val updatedAt: String
)

enum class SubscriptionPlan {
    BASIC,
    PRO
}

enum class SubscriptionStatus {
    ACTIVE,
    CANCELLED,
    PAST_DUE,
    TRIALING
}

data class UsageLimits(
    val aiChatMessagesPerDay: Int?,
    val facialAnalysisPerWeek: Int?,
    val contentRecommendationsPerWeek: Int?,
    val checkInsPerDay: Int?,
    val maxPatientConnections: Int?,
    val contentAssignmentsPerWeek: Int?
)
