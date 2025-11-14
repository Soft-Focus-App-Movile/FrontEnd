package com.softfocus.features.notifications.presentation.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.softfocus.core.data.local.UserSession
import com.softfocus.features.auth.domain.models.UserType
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
import java.time.LocalDateTime
import java.time.LocalTime
import javax.inject.Inject

data class NotificationsState(
    val notifications: List<Notification> = emptyList(),
    val isLoading: Boolean = false,
    val isRefreshing: Boolean = false,
    val error: String? = null,
    val unreadCount: Int = 0,
    val notificationsEnabled: Boolean = true
)

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

    // ✅ NUEVO: Guardar notificaciones antes de desactivar
    private var notificationsBeforeDisable = listOf<Notification>()

    // ✅ NUEVO: Timestamp de cuando se desactivaron las notificaciones
    private var disabledTimestamp: LocalDateTime? = null

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

            // PASO 1: Cargar preferencias
            val preferencesResult = getPreferencesUseCase(userId)
            val masterPreference = preferencesResult.getOrNull()?.firstOrNull()
            val notificationsEnabled = masterPreference?.isEnabled ?: true
            val schedule = masterPreference?.schedule

            println("🔔 [VIEWMODEL] Preferencias cargadas:")
            println("   - Usuario: ${userType?.name}")
            println("   - Notificaciones activadas: $notificationsEnabled")
            println("   - Horario: ${schedule?.startTime} - ${schedule?.endTime}")
            println("   - disabled_at del backend: ${masterPreference?.disabledAt}")

            // ✅ PASO 1.5: Gestionar timestamp de desactivación
            val backendDisabledAt = masterPreference?.disabledAt

            // PRIORIDAD: Siempre usar el timestamp del backend si existe
            if (backendDisabledAt != null && !notificationsEnabled) {
                // Notificaciones desactivadas CON timestamp del backend
                disabledTimestamp = backendDisabledAt
                println("📡 [VIEWMODEL] Usando disabled_at del BACKEND: $disabledTimestamp")
            } else if (notificationsEnabled) {
                // Notificaciones activadas = limpiar timestamp
                disabledTimestamp = null
                println("🟢 [VIEWMODEL] Notificaciones ACTIVADAS - Limpiando timestamp")
            } else {
                // Fallback: desactivadas pero sin timestamp del backend (no debería pasar)
                disabledTimestamp = LocalDateTime.now()
                println("⚠️ [VIEWMODEL] FALLBACK - Usando timestamp local: $disabledTimestamp")
            }

            // PASO 2: Cargar notificaciones
            val result = getNotificationsUseCase(
                userId = userId,
                status = null
            )

            result.fold(
                onSuccess = { notifications ->
                    allNotifications = notifications

                    // ✅ Si las notificaciones ESTÁN ACTIVADAS, guardar como "antes de desactivar"
                    if (notificationsEnabled) {
                        notificationsBeforeDisable = notifications
                        println("💾 [VIEWMODEL] Guardadas ${notifications.size} notificaciones como backup")
                    } else {
                        println("⚠️ [VIEWMODEL] Notificaciones desactivadas desde: $disabledTimestamp")

                        // ✅ Si están desactivadas, filtrar solo las posteriores a disabled_at
                        if (disabledTimestamp != null) {
                            val newNotifications = notifications.filter { notif ->
                                notif.createdAt.isAfter(disabledTimestamp)
                            }

                            println("📊 [VIEWMODEL] Notificaciones nuevas después de desactivar: ${newNotifications.size}")

                            // Combinar notificaciones viejas + nuevas
                            allNotifications = notificationsBeforeDisable + newNotifications
                            println("📦 [VIEWMODEL] Total (backup + nuevas): ${allNotifications.size}")
                        } else {
                            // Fallback: usar backup si no hay timestamp
                            allNotifications = notificationsBeforeDisable
                            println("⚠️ [VIEWMODEL] Sin timestamp - Usando backup de ${notificationsBeforeDisable.size}")
                        }
                    }

                    // PASO 3: Aplicar filtros según usuario y preferencias
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

        // Si las notificaciones están DESACTIVADAS, no mostrar nada
        if (!notificationsEnabled) {
            println("🚫 [VIEWMODEL] Notificaciones desactivadas - Ocultando todas")
            _state.value = _state.value.copy(notifications = emptyList())
            return
        }

        // Filtrar por tab (Todas vs No leídas)
        var filtered = when (currentFilter) {
            DeliveryStatus.DELIVERED -> allNotifications.filter { it.readAt == null }
            else -> allNotifications
        }

        // LÓGICA ESPECIAL PARA PSICÓLOGOS: Filtrar por horario
        if (currentUserType == UserType.PSYCHOLOGIST && schedule != null) {
            val now = LocalTime.now()
            val startTime = schedule.startTime
            val endTime = schedule.endTime

            // Verificar si estamos dentro del horario
            val isWithinSchedule = if (endTime.isAfter(startTime)) {
                // Horario normal (ej: 9:00 - 17:00)
                now.isAfter(startTime) && now.isBefore(endTime)
            } else {
                // Horario que cruza medianoche (ej: 22:00 - 06:00)
                now.isAfter(startTime) || now.isBefore(endTime)
            }

            println("⏰ [VIEWMODEL] Verificación de horario:")
            println("   - Hora actual: $now")
            println("   - Horario: $startTime - $endTime")
            println("   - Dentro del horario: $isWithinSchedule")

            if (!isWithinSchedule) {
                // FUERA DEL HORARIO: Solo mostrar alertas de crisis/emergencia
                filtered = filtered.filter { notification ->
                    notification.type == NotificationType.CRISIS_ALERT ||
                            notification.type == NotificationType.EMERGENCY ||
                            notification.priority == Priority.CRITICAL ||
                            notification.priority == Priority.HIGH
                }
                println("🚨 [VIEWMODEL] Fuera de horario - Mostrando solo ${filtered.size} alertas críticas")
            } else {
                println("✅ [VIEWMODEL] Dentro de horario - Mostrando todas las ${filtered.size} notificaciones")
            }
        }

        println("📊 [VIEWMODEL] Total notificaciones mostradas: ${filtered.size}")
        _state.value = _state.value.copy(notifications = filtered)
    }

    fun filterNotifications(status: DeliveryStatus?) {
        currentFilter = status

        // Re-cargar preferencias y aplicar filtro
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
            println("🔵 [VIEWMODEL] Marcando como leída: $notificationId")

            markAsReadUseCase(notificationId).fold(
                onSuccess = {
                    println("✅ [VIEWMODEL] Marcado como leída exitosamente")

                    allNotifications = allNotifications.map { notification ->
                        if (notification.id == notificationId) {
                            notification.copy(
                                status = DeliveryStatus.READ,
                                readAt = LocalDateTime.now()
                            )
                        } else {
                            notification
                        }
                    }

                    // Re-aplicar filtros
                    val userId = userSession.getUser()?.id
                    val userType = userSession.getUser()?.userType
                    if (userId != null) {
                        val preferencesResult = getPreferencesUseCase(userId)
                        val schedule = preferencesResult.getOrNull()?.firstOrNull()?.schedule
                        applyFilter(userType, _state.value.notificationsEnabled, schedule)
                    }

                    loadUnreadCount()
                },
                onFailure = { error ->
                    println("❌ [VIEWMODEL] Error al marcar como leída: ${error.message}")
                }
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
                            readAt = notification.readAt ?: LocalDateTime.now()
                        )
                    }

                    // Re-aplicar filtros
                    val preferencesResult = getPreferencesUseCase(userId)
                    val schedule = preferencesResult.getOrNull()?.firstOrNull()?.schedule
                    applyFilter(userType, _state.value.notificationsEnabled, schedule)

                    loadUnreadCount()
                },
                onFailure = { /* Ignorar error */ }
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

                    // Re-aplicar filtros
                    val preferencesResult = getPreferencesUseCase(userId)
                    val schedule = preferencesResult.getOrNull()?.firstOrNull()?.schedule
                    applyFilter(userType, _state.value.notificationsEnabled, schedule)

                    loadUnreadCount()
                },
                onFailure = { /* Ignorar error */ }
            )
        }
    }

    private fun loadUnreadCount() {
        viewModelScope.launch {
            val userId = userSession.getUser()?.id ?: return@launch

            notificationRepository.getUnreadCount(userId).fold(
                onSuccess = { count ->
                    // Si las notificaciones están desactivadas, mostrar 0
                    val finalCount = if (_state.value.notificationsEnabled) count else 0
                    _state.value = _state.value.copy(unreadCount = finalCount)
                },
                onFailure = { /* Ignorar error */ }
            )
        }
    }

    fun refreshNotifications() {
        viewModelScope.launch {
            println("🔄 [VIEWMODEL] Refresh manual iniciado")
            _state.value = _state.value.copy(isRefreshing = true, error = null)

            val userId = userSession.getUser()?.id ?: return@launch
            val userType = userSession.getUser()?.userType

            // Recargar preferencias
            val preferencesResult = getPreferencesUseCase(userId)
            val masterPreference = preferencesResult.getOrNull()?.firstOrNull()
            val notificationsEnabled = masterPreference?.isEnabled ?: true
            val schedule = masterPreference?.schedule

            // ✅ Actualizar disabled_at desde el backend
            val backendDisabledAt = masterPreference?.disabledAt
            if (backendDisabledAt != null) {
                disabledTimestamp = backendDisabledAt
                println("📡 [VIEWMODEL] Refresh - disabled_at del backend: $disabledTimestamp")
            }

            // Recargar notificaciones
            val result = notificationRepository.getNotifications(
                userId = userId,
                status = null,
                page = 1,
                size = 20
            )

            result.fold(
                onSuccess = { notifications ->
                    println("✅ [VIEWMODEL] Refresh exitoso: ${notifications.size} notificaciones")

                    // ✅ Si las notificaciones ESTÁN ACTIVADAS, actualizar todo
                    if (notificationsEnabled) {
                        allNotifications = notifications
                        notificationsBeforeDisable = notifications
                        println("💾 [VIEWMODEL] Actualizado backup: ${notifications.size} notificaciones")
                    } else {
                        println("⚠️ [VIEWMODEL] Notificaciones desactivadas - Filtrando por timestamp")

                        // ✅ Si están desactivadas, filtrar solo las nuevas
                        if (disabledTimestamp != null) {
                            val newNotifications = notifications.filter { notif ->
                                notif.createdAt.isAfter(disabledTimestamp)
                            }
                            allNotifications = notificationsBeforeDisable + newNotifications
                            println("📦 [VIEWMODEL] Total (backup + ${newNotifications.size} nuevas): ${allNotifications.size}")
                        } else {
                            allNotifications = notificationsBeforeDisable
                            println("⚠️ [VIEWMODEL] Sin timestamp - Usando backup")
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
                    println("❌ [VIEWMODEL] Error en refresh: ${error.message}")
                    _state.value = _state.value.copy(
                        isRefreshing = false,
                        error = error.message ?: "Error al actualizar"
                    )
                }
            )
        }
    }
}