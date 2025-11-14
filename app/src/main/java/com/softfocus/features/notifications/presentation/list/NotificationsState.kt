package com.softfocus.features.notifications.presentation.list

import com.softfocus.features.notifications.domain.models.Notification

data class NotificationsState(
    val notifications: List<Notification> = emptyList(),
    val isLoading: Boolean = false,
    val isRefreshing: Boolean = false,
    val error: String? = null,
    val unreadCount: Int = 0,
    val notificationsEnabled: Boolean = true
)