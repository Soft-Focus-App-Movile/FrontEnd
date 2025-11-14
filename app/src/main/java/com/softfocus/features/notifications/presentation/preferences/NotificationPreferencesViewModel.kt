package com.softfocus.features.notifications.presentation.preferences

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.softfocus.core.data.local.UserSession
import com.softfocus.features.notifications.domain.models.*
import com.softfocus.features.notifications.domain.usecases.GetNotificationPreferencesUseCase
import com.softfocus.features.notifications.domain.usecases.UpdateNotificationPreferencesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.time.LocalTime
import javax.inject.Inject

@HiltViewModel
class NotificationPreferencesViewModel @Inject constructor(
    private val getPreferencesUseCase: GetNotificationPreferencesUseCase,
    private val updatePreferencesUseCase: UpdateNotificationPreferencesUseCase,
    private val userSession: UserSession
) : ViewModel() {

    private val _state = MutableStateFlow(NotificationPreferencesState())
    val state: StateFlow<NotificationPreferencesState> = _state.asStateFlow()

    init {
        loadPreferences()
    }

    fun loadPreferences() {
        viewModelScope.launch {
            try {
                _state.value = _state.value.copy(isLoading = true, error = null)

                val userId = userSession.getUser()?.id
                if (userId == null) {
                    _state.value = _state.value.copy(
                        isLoading = false,
                        error = "Usuario no autenticado"
                    )
                    return@launch
                }

                getPreferencesUseCase(userId).fold(
                    onSuccess = { preferences ->
                        val enrichedPreferences = ensureMainPreferences(preferences)
                        _state.value = _state.value.copy(
                            preferences = enrichedPreferences,
                            isLoading = false
                        )
                    },
                    onFailure = { error ->
                        _state.value = _state.value.copy(
                            isLoading = false,
                            error = error.message ?: "Error al cargar preferencias"
                        )
                    }
                )
            } catch (e: Exception) {
                _state.value = _state.value.copy(
                    isLoading = false,
                    error = "Error inesperado al cargar: ${e.message}"
                )
            }
        }
    }

    fun toggleMasterPreference() {
        viewModelScope.launch {
            try {
                _state.value = _state.value.copy(isSaving = true, successMessage = null, error = null)

                val masterPreference = _state.value.preferences.firstOrNull()
                if (masterPreference == null) {
                    _state.value = _state.value.copy(
                        isSaving = false,
                        error = "No hay preferencias configuradas"
                    )
                    return@launch
                }

                val newEnabled = !masterPreference.isEnabled
                val updated = masterPreference.copy(isEnabled = newEnabled)
                val updatedList = listOf(updated)

                updatePreferencesUseCase(updatedList).fold(
                    onSuccess = { serverPreferences ->
                        val finalPreferences = serverPreferences.ifEmpty {
                            updatedList
                        }

                        _state.value = _state.value.copy(
                            preferences = finalPreferences,
                            isSaving = false,
                            successMessage = if (newEnabled) "Notificaciones activadas" else "Notificaciones desactivadas"
                        )

                        kotlinx.coroutines.delay(3000)
                        _state.value = _state.value.copy(successMessage = null)
                    },
                    onFailure = { error ->
                        _state.value = _state.value.copy(
                            isSaving = false,
                            error = error.message ?: "Error al actualizar"
                        )
                    }
                )
            } catch (e: Exception) {
                _state.value = _state.value.copy(
                    isSaving = false,
                    error = "Error inesperado: ${e.message}"
                )
            }
        }
    }

    fun updateSchedule(startTime: LocalTime, endTime: LocalTime) {
        viewModelScope.launch {
            try {
                _state.value = _state.value.copy(isSaving = true, successMessage = null, error = null)

                val masterPreference = _state.value.preferences.firstOrNull()
                if (masterPreference == null) {
                    _state.value = _state.value.copy(
                        isSaving = false,
                        error = "No hay preferencias configuradas"
                    )
                    return@launch
                }

                val newSchedule = NotificationSchedule(
                    startTime = startTime,
                    endTime = endTime,
                    daysOfWeek = listOf(1, 2, 3, 4, 5, 6, 7)
                )

                val updated = masterPreference.copy(schedule = newSchedule)
                val updatedList = listOf(updated)

                updatePreferencesUseCase(updatedList).fold(
                    onSuccess = { serverPreferences ->
                        val finalPreferences = serverPreferences.ifEmpty {
                            updatedList
                        }

                        _state.value = _state.value.copy(
                            preferences = finalPreferences,
                            isSaving = false,
                            successMessage = "Horario actualizado correctamente"
                        )

                        kotlinx.coroutines.delay(3000)
                        _state.value = _state.value.copy(successMessage = null)
                    },
                    onFailure = { error ->
                        _state.value = _state.value.copy(
                            isSaving = false,
                            error = error.message ?: "Error al actualizar horario"
                        )
                    }
                )
            } catch (e: Exception) {
                _state.value = _state.value.copy(
                    isSaving = false,
                    error = "Error inesperado: ${e.message}"
                )
            }
        }
    }

    private fun ensureMainPreferences(
        preferences: List<NotificationPreference>
    ): List<NotificationPreference> {
        try {
            val mainTypes = listOf(
                NotificationType.CHECKIN_REMINDER,
                NotificationType.INFO,
                NotificationType.SYSTEM_UPDATE
            )

            val filteredPrefs = preferences.filter { it.notificationType in mainTypes }
            val mutablePrefs = mutableListOf<NotificationPreference>()

            val checkInPref = filteredPrefs.find { it.notificationType == NotificationType.CHECKIN_REMINDER }
            if (checkInPref != null) {
                val prefWithSchedule = if (checkInPref.schedule == null) {
                    checkInPref.copy(
                        schedule = NotificationSchedule(
                            startTime = LocalTime.of(9, 0),
                            endTime = LocalTime.of(9, 0),
                            daysOfWeek = listOf(1, 2, 3, 4, 5, 6, 7)
                        )
                    )
                } else {
                    checkInPref
                }
                mutablePrefs.add(prefWithSchedule)
            } else {
                mutablePrefs.add(
                    NotificationPreference(
                        id = "checkin_reminder_local",
                        userId = userSession.getUser()?.id ?: "",
                        notificationType = NotificationType.CHECKIN_REMINDER,
                        isEnabled = true,
                        deliveryMethod = DeliveryMethod.PUSH,
                        schedule = NotificationSchedule(
                            startTime = LocalTime.of(9, 0),
                            endTime = LocalTime.of(9, 0),
                            daysOfWeek = listOf(1, 2, 3, 4, 5, 6, 7)
                        )
                    )
                )
            }

            val infoPref = filteredPrefs.find { it.notificationType == NotificationType.INFO }
            if (infoPref != null) {
                mutablePrefs.add(infoPref)
            } else {
                mutablePrefs.add(
                    NotificationPreference(
                        id = "daily_suggestions_local",
                        userId = userSession.getUser()?.id ?: "",
                        notificationType = NotificationType.INFO,
                        isEnabled = true,
                        deliveryMethod = DeliveryMethod.PUSH,
                        schedule = null
                    )
                )
            }

            val systemPref = filteredPrefs.find { it.notificationType == NotificationType.SYSTEM_UPDATE }
            if (systemPref != null) {
                mutablePrefs.add(systemPref)
            } else {
                mutablePrefs.add(
                    NotificationPreference(
                        id = "promotions_local",
                        userId = userSession.getUser()?.id ?: "",
                        notificationType = NotificationType.SYSTEM_UPDATE,
                        isEnabled = true,
                        deliveryMethod = DeliveryMethod.PUSH,
                        schedule = null
                    )
                )
            }

            return mutablePrefs.sortedBy {
                when (it.notificationType) {
                    NotificationType.CHECKIN_REMINDER -> 1
                    NotificationType.INFO -> 2
                    NotificationType.SYSTEM_UPDATE -> 3
                    else -> 4
                }
            }
        } catch (_: Exception) {
            return emptyList()
        }
    }

}