package com.softfocus.features.subscription.domain.models

data class FeatureAccess(
    val hasAccess: Boolean,
    val denialReason: String?,
    val currentUsage: Int?,
    val limit: Int?,
    val upgradeMessage: String?
)
