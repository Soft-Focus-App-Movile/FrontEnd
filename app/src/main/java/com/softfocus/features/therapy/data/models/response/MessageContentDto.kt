package com.softfocus.features.therapy.data.models.response

import com.google.gson.annotations.SerializedName

/**
 * DTO para el Value Object MessageContent que viene del backend.
 * El backend envía: { "content": { "value": "Hola" } }
 */
data class MessageContentDto(
    @SerializedName("value")
    val value: String
)