package com.softfocus.features.notifications.presentation.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.softfocus.core.data.local.UserSession
import com.softfocus.features.auth.domain.models.UserType
import com.softfocus.features.notifications.presentation.list.NotificationsState
import com.softfocus.features.notifications.domain.models.DeliveryStatus
import com.softfocus.features.notifications.domain.models.Notification
import com.softfocus.features.notifications.domain.models.NotificationType
import com.softfocus.features.notifications.domain.models.Priority
import com.softfocus.features.notifications.domain.usecases.GetNotificationsUseCase
import com.softfocus.features.notifications.domain.usecases.MarkAsReadUseCase
import com.softfocus.features.notifications.domain.usecases.GetNotificationPreferencesUseCase
import com.softfocus.features.notifications.domain.repositories.NotificationRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.time.LocalTime
import javax.inject.Inject

@HiltViewModel
class NotificationsViewModel @Inject constructor(
    private val getNotificationsUseCase: GetNotificationsUseCase,
    private val markAsReadUseCase: MarkAsReadUseCase,
    private val getPreferencesUseCase: GetNotificationPreferencesUseCase,
    private val notificationRepository: NotificationRepository,
    private val userSession: UserSession
) : ViewModel() {

    private val _state = MutableStateFlow(NotificationsState())
    val state: StateFlow<NotificationsState> = _state.asStateFlow()

    private var currentFilter: DeliveryStatus? = null
    private var allNotifications = listOf<Notification>()

    init {
        loadNotifications()
        loadUnreadCount()
    }

    fun loadNotifications() {
        viewModelScope.launch {
            _state.value = _state.value.copy(isLoading = true, error = null)

            val userId = userSession.getUser()?.id
            val userType = userSession.getUser()?.userType

            if (userId == null) {
                _state.value = _state.value.copy(
                    isLoading = false,
                    error = "Usuario no autenticado"
                )
                return@launch
            }

            val preferencesResult = getPreferencesUseCase(userId)
            val masterPreference = preferencesResult.getOrNull()?.firstOrNull()
            val notificationsEnabled = masterPreference?.isEnabled ?: true
            val schedule = masterPreference?.schedule
            val disabledAt = masterPreference?.disabledAt

            val result = getNotificationsUseCase(userId = userId, status = null)

            result.fold(
                onSuccess = { notifications ->
                    allNotifications = notifications

                    if (!notificationsEnabled && disabledAt != null) {
                        allNotifications = notifications.filter {
                            it.createdAt.isBefore(disabledAt) || it.createdAt.isEqual(disabledAt)
                        }
                    }

                    _state.value = _state.value.copy(
                        notificationsEnabled = notificationsEnabled,
                        isLoading = false
                    )

                    applyFilter(userType, notificationsEnabled, schedule)
                },
                onFailure = { error ->
                    _state.value = _state.value.copy(
                        isLoading = false,
                        error = error.message ?: "Error al cargar notificaciones"
                    )
                }
            )
        }
    }

    private fun applyFilter(
        userType: UserType? = null,
        notificationsEnabled: Boolean = _state.value.notificationsEnabled,
        schedule: com.softfocus.features.notifications.domain.models.NotificationSchedule? = null
    ) {
        val currentUserType = userType ?: userSession.getUser()?.userType

        var filtered = when (currentFilter) {
            DeliveryStatus.DELIVERED -> allNotifications.filter { it.readAt == null }
            else -> allNotifications
        }

        if (notificationsEnabled && currentUserType == UserType.PSYCHOLOGIST && schedule != null) {
            val now = LocalTime.now()
            val startTime = schedule.startTime
            val endTime = schedule.endTime

            val isWithinSchedule = if (endTime.isAfter(startTime)) {
                now.isAfter(startTime) && now.isBefore(endTime)
            } else {
                now.isAfter(startTime) || now.isBefore(endTime)
            }

            if (!isWithinSchedule) {
                filtered = filtered.filter { notification ->
                    val wasAlreadyRead = notification.readAt != null
                    val isCritical = notification.type == NotificationType.CRISIS_ALERT ||
                            notification.type == NotificationType.EMERGENCY ||
                            notification.priority == Priority.CRITICAL ||
                            notification.priority == Priority.HIGH

                    wasAlreadyRead || isCritical
                }
            }
        }

        _state.value = _state.value.copy(notifications = filtered)
    }

    fun filterNotifications(status: DeliveryStatus?) {
        currentFilter = status

        viewModelScope.launch {
            val userId = userSession.getUser()?.id ?: return@launch
            val userType = userSession.getUser()?.userType

            val preferencesResult = getPreferencesUseCase(userId)
            val masterPreference = preferencesResult.getOrNull()?.firstOrNull()
            val schedule = masterPreference?.schedule

            applyFilter(userType, _state.value.notificationsEnabled, schedule)
        }
    }

    fun markAsRead(notificationId: String) {
        viewModelScope.launch {
            markAsReadUseCase(notificationId).fold(
                onSuccess = {
                    allNotifications = allNotifications.map { notification ->
                        if (notification.id == notificationId) {
                            notification.copy(
                                status = DeliveryStatus.READ,
                                readAt = java.time.LocalDateTime.now()
                            )
                        } else {
                            notification
                        }
                    }

                    val userId = userSession.getUser()?.id
                    val userType = userSession.getUser()?.userType
                    if (userId != null) {
                        val preferencesResult = getPreferencesUseCase(userId)
                        val schedule = preferencesResult.getOrNull()?.firstOrNull()?.schedule
                        applyFilter(userType, _state.value.notificationsEnabled, schedule)
                    }

                    loadUnreadCount()
                },
                onFailure = { }
            )
        }
    }

    fun markAllAsRead() {
        viewModelScope.launch {
            val userId = userSession.getUser()?.id ?: return@launch
            val userType = userSession.getUser()?.userType

            notificationRepository.markAllAsRead(userId).fold(
                onSuccess = {
                    allNotifications = allNotifications.map { notification ->
                        notification.copy(
                            status = DeliveryStatus.READ,
                            readAt = notification.readAt ?: java.time.LocalDateTime.now()
                        )
                    }

                    val preferencesResult = getPreferencesUseCase(userId)
                    val schedule = preferencesResult.getOrNull()?.firstOrNull()?.schedule
                    applyFilter(userType, _state.value.notificationsEnabled, schedule)

                    loadUnreadCount()
                },
                onFailure = { }
            )
        }
    }

    fun deleteNotification(notificationId: String) {
        viewModelScope.launch {
            val userId = userSession.getUser()?.id ?: return@launch
            val userType = userSession.getUser()?.userType

            notificationRepository.deleteNotification(notificationId).fold(
                onSuccess = {
                    allNotifications = allNotifications.filter { it.id != notificationId }

                    val preferencesResult = getPreferencesUseCase(userId)
                    val schedule = preferencesResult.getOrNull()?.firstOrNull()?.schedule
                    applyFilter(userType, _state.value.notificationsEnabled, schedule)

                    loadUnreadCount()
                },
                onFailure = { }
            )
        }
    }

    private fun loadUnreadCount() {
        viewModelScope.launch {
            val userId = userSession.getUser()?.id ?: return@launch

            notificationRepository.getUnreadCount(userId).fold(
                onSuccess = { count ->
                    val finalCount = if (_state.value.notificationsEnabled) count else 0
                    _state.value = _state.value.copy(unreadCount = finalCount)
                },
                onFailure = { }
            )
        }
    }

    fun refreshNotifications() {
        viewModelScope.launch {
            _state.value = _state.value.copy(isRefreshing = true, error = null)

            val userId = userSession.getUser()?.id ?: return@launch
            val userType = userSession.getUser()?.userType

            val preferencesResult = getPreferencesUseCase(userId)
            val masterPreference = preferencesResult.getOrNull()?.firstOrNull()
            val notificationsEnabled = masterPreference?.isEnabled ?: true
            val schedule = masterPreference?.schedule
            val disabledAt = masterPreference?.disabledAt

            val result = notificationRepository.getNotifications(
                userId = userId,
                status = null,
                page = 1,
                size = 20
            )

            result.fold(
                onSuccess = { notifications ->
                    allNotifications = notifications

                    if (!notificationsEnabled && disabledAt != null) {
                        allNotifications = notifications.filter {
                            it.createdAt.isBefore(disabledAt) || it.createdAt.isEqual(disabledAt)
                        }
                    }

                    _state.value = _state.value.copy(
                        notificationsEnabled = notificationsEnabled,
                        isRefreshing = false
                    )
                    applyFilter(userType, notificationsEnabled, schedule)
                    loadUnreadCount()
                },
                onFailure = { error ->
                    _state.value = _state.value.copy(
                        isRefreshing = false,
                        error = error.message ?: "Error al actualizar"
                    )
                }
            )
        }
    }
}