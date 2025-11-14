package com.softfocus.features.notifications.data.models.response

import com.google.gson.annotations.SerializedName
import com.softfocus.features.notifications.domain.models.*
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

data class NotificationPreferenceResponseDto(
    @SerializedName("id")
    val id: String,

    @SerializedName("user_id")
    val userId: String,

    @SerializedName("notification_type")
    val notificationType: String,

    @SerializedName("is_enabled")
    val isEnabled: Boolean,

    @SerializedName("schedule")
    val schedule: ScheduleDto?,

    @SerializedName("delivery_method")
    val deliveryMethod: String,

    @SerializedName("disabled_at")
    val disabledAt: String? = null
) {
    fun toDomain(): NotificationPreference {
        return NotificationPreference(
            id = id,
            userId = userId,
            notificationType = parseNotificationType(notificationType),
            isEnabled = isEnabled,
            schedule = schedule?.toDomain(),
            deliveryMethod = parseDeliveryMethod(deliveryMethod),
            disabledAt = disabledAt?.let { parseDateTime(it) }
        )
    }

    private fun parseNotificationType(type: String): NotificationType {
        return try {
            val normalized = type.uppercase().replace("-", "_")
            NotificationType.valueOf(normalized)
        } catch (e: IllegalArgumentException) {
            android.util.Log.w("NotificationPref", "Unknown notification type: $type, using INFO")
            NotificationType.INFO
        }
    }

    private fun parseDeliveryMethod(method: String): DeliveryMethod {
        return try {
            DeliveryMethod.valueOf(method.uppercase())
        } catch (e: IllegalArgumentException) {
            android.util.Log.w("NotificationPref", "Unknown delivery method: $method, using PUSH")
            DeliveryMethod.PUSH
        }
    }

    private fun parseDateTime(dateTimeStr: String): LocalDateTime? {
        return try {
            LocalDateTime.parse(
                dateTimeStr.replace("Z", ""),
                DateTimeFormatter.ISO_LOCAL_DATE_TIME
            )
        } catch (e: Exception) {
            try {
                LocalDateTime.parse(dateTimeStr, DateTimeFormatter.ISO_LOCAL_DATE_TIME)
            } catch (e2: Exception) {
                try {
                    val cleaned = dateTimeStr.replace("Z", "")
                    LocalDateTime.parse(
                        cleaned.substring(0, 19),
                        DateTimeFormatter.ISO_LOCAL_DATE_TIME
                    )
                } catch (e3: Exception) {
                    android.util.Log.e("NotificationPref", "Error parsing disabled_at: $dateTimeStr", e3)
                    null
                }
            }
        }
    }
}
