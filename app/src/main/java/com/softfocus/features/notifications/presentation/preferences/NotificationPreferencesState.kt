package com.softfocus.features.notifications.presentation.preferences

import com.softfocus.features.notifications.domain.models.NotificationPreference

data class NotificationPreferencesState(
    val preferences: List<NotificationPreference> = emptyList(),
    val isLoading: Boolean = false,
    val isSaving: Boolean = false,
    val error: String? = null,
    val successMessage: String? = null
)