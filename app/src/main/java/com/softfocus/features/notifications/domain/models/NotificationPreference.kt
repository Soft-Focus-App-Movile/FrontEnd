package com.softfocus.features.notifications.domain.models

import java.time.LocalDateTime

data class NotificationPreference(
    val id: String,
    val userId: String,
    val notificationType: NotificationType,
    val isEnabled: Boolean,
    val deliveryMethod: DeliveryMethod,
    val schedule: NotificationSchedule? = null,
    val disabledAt: LocalDateTime? = null
)