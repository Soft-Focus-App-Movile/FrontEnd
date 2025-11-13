package com.softfocus.features.therapy.data.models.response

import com.google.gson.annotations.SerializedName

/**
 * DTO para la respuesta de chat/send
 */
data class SendChatMessageResponseDto(
    @SerializedName("messageId")
    val messageId: String
)