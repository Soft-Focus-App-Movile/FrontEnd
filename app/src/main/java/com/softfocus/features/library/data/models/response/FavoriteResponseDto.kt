package com.softfocus.features.library.data.models.response

import com.google.gson.annotations.SerializedName
import com.softfocus.features.library.domain.models.Favorite
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

/**
 * DTO para respuesta de favorito del backend
 */
data class FavoriteResponseDto(
    @SerializedName("favoriteId")
    val id: String?,

    @SerializedName("userId")
    val userId: String?,

    @SerializedName("content")
    val content: ContentItemResponseDto?,

    @SerializedName("addedAt")
    val addedAt: String? // ISO 8601 format: "2025-11-02T10:30:00"
) {
    /**
     * Mapea el DTO a la entidad de dominio
     * Retorna null si faltan datos requeridos
     *
     * Nota: userId es opcional porque GET /favorites no lo devuelve
     */
    fun toDomain(fallbackUserId: String? = null): Favorite? {
        // Validar que tengamos los datos mínimos necesarios
        if (id == null || content == null || addedAt == null) {
            return null
        }

        // Usar userId del response o el fallback proporcionado
        val finalUserId = userId ?: fallbackUserId ?: ""

        return Favorite(
            id = id,
            userId = finalUserId,
            content = content.toDomain(),
            addedAt = parseDateTime(addedAt)
        )
    }

    companion object {
        /**
         * Parsea un string de fecha/hora a LocalDateTime
         */
        private fun parseDateTime(dateTimeString: String): LocalDateTime {
            return try {
                LocalDateTime.parse(dateTimeString, DateTimeFormatter.ISO_DATE_TIME)
            } catch (e: Exception) {
                // Fallback: intenta otros formatos comunes
                try {
                    LocalDateTime.parse(dateTimeString)
                } catch (e: Exception) {
                    LocalDateTime.now() // Último fallback
                }
            }
        }
    }
}
