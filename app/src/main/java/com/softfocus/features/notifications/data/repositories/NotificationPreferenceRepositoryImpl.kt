package com.softfocus.features.notifications.data.repositories

import com.softfocus.features.notifications.data.models.request.NotificationPreferenceDto
import com.softfocus.features.notifications.data.models.request.NotificationScheduleDto
import com.softfocus.features.notifications.data.models.request.UpdatePreferencesRequestDto
import com.softfocus.features.notifications.data.remote.NotificationService
import com.softfocus.features.notifications.domain.models.NotificationPreference
import com.softfocus.features.notifications.domain.repositories.NotificationPreferenceRepository
import java.time.format.DateTimeFormatter
import javax.inject.Inject

class NotificationPreferenceRepositoryImpl @Inject constructor(
    private val notificationService: NotificationService
) : NotificationPreferenceRepository {

    override suspend fun getPreferences(userId: String): Result<List<NotificationPreference>> {
        return try {
            val response = notificationService.getPreferences()

            if (response.isSuccessful && response.body() != null) {
                val body = response.body()!!
                val preferences = body.preferences?.map { it.toDomain() } ?: emptyList()

                // ✅ Log detallado para verificar que disabled_at llegue correctamente
                preferences.forEach { pref ->
                    android.util.Log.d(
                        "NotifPrefRepo",
                        "📡 Preference ${pref.notificationType}: " +
                                "enabled=${pref.isEnabled}, " +
                                "disabled_at=${pref.disabledAt}, " +
                                "schedule=${pref.schedule}"
                    )
                }

                Result.success(preferences)
            } else {
                val errorMsg = "Error al obtener preferencias: ${response.code()} - ${response.message()}"
                android.util.Log.e("NotifPrefRepo", errorMsg)
                Result.failure(Exception(errorMsg))
            }
        } catch (e: Exception) {
            android.util.Log.e("NotifPrefRepo", "❌ Error de red al obtener preferencias", e)
            Result.failure(Exception("Error de red al obtener preferencias: ${e.message}", e))
        }
    }

    override suspend fun updatePreference(preference: NotificationPreference): Result<NotificationPreference> {
        return updatePreferences(listOf(preference)).map {
            it.firstOrNull() ?: preference
        }
    }

    override suspend fun updatePreferences(preferences: List<NotificationPreference>): Result<List<NotificationPreference>> {
        return try {
            val formatter = DateTimeFormatter.ofPattern("HH:mm")

            // Mapear las preferencias al formato del backend
            val preferenceDtos = preferences.map { pref ->
                val scheduleDto = pref.schedule?.let {
                    NotificationScheduleDto(
                        startTime = it.startTime.format(formatter),
                        endTime = it.endTime.format(formatter),
                        daysOfWeek = it.daysOfWeek
                    )
                }

                android.util.Log.d(
                    "NotifPrefRepo",
                    "📤 Mapeando ${pref.notificationType}: " +
                            "enabled=${pref.isEnabled}, " +
                            "schedule=${scheduleDto}"
                )

                NotificationPreferenceDto(
                    notificationType = pref.notificationType.name
                        .lowercase()
                        .replace("_", "-"),
                    isEnabled = pref.isEnabled,
                    schedule = scheduleDto,
                    deliveryMethod = pref.deliveryMethod.name.lowercase()
                )
            }

            android.util.Log.d(
                "NotifPrefRepo",
                "📤 Enviando al backend: ${preferenceDtos.size} preferencias"
            )

            val response = notificationService.updatePreferences(
                UpdatePreferencesRequestDto(preferenceDtos)
            )

            android.util.Log.d("NotifPrefRepo", "📡 Response code: ${response.code()}")

            if (response.isSuccessful && response.body() != null) {
                val body = response.body()!!
                val updatedPreferences = body.preferences?.map { it.toDomain() } ?: emptyList()

                // ✅ Log de respuesta con disabled_at
                updatedPreferences.forEach { pref ->
                    android.util.Log.d(
                        "NotifPrefRepo",
                        "📥 Updated ${pref.notificationType}: " +
                                "enabled=${pref.isEnabled}, " +
                                "disabled_at=${pref.disabledAt}"
                    )
                }

                Result.success(updatedPreferences)
            } else {
                val errorMsg = "Error al actualizar preferencias: ${response.code()} - ${response.message()}"
                android.util.Log.e("NotifPrefRepo", "❌ $errorMsg")
                Result.failure(Exception(errorMsg))
            }
        } catch (e: Exception) {
            android.util.Log.e("NotifPrefRepo", "❌ Excepción al actualizar preferencias", e)
            Result.failure(Exception("Error de red al actualizar preferencias: ${e.message}", e))
        }
    }

    override suspend fun resetToDefaults(userId: String): Result<List<NotificationPreference>> {
        return try {
            val response = notificationService.resetPreferences()

            if (response.isSuccessful && response.body() != null) {
                val body = response.body()!!
                val preferences = body.preferences?.map { it.toDomain() } ?: emptyList()

                android.util.Log.d(
                    "NotifPrefRepo",
                    "🔄 Reset to defaults: ${preferences.size} preferencias"
                )

                Result.success(preferences)
            } else {
                val errorMsg = "Error al resetear preferencias: ${response.code()} - ${response.message()}"
                android.util.Log.e("NotifPrefRepo", errorMsg)
                Result.failure(Exception(errorMsg))
            }
        } catch (e: Exception) {
            android.util.Log.e("NotifPrefRepo", "❌ Error de red al resetear preferencias", e)
            Result.failure(Exception("Error de red al resetear preferencias: ${e.message}", e))
        }
    }
}