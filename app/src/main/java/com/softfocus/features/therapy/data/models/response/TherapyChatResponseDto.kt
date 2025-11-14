package com.softfocus.features.therapy.data.models.response

import com.google.gson.annotations.SerializedName

/**
 * DTO para un mensaje de chat (usado en History y LastReceived)
 */
data class TherapyChatResponseDto(
    @SerializedName("id")
    val id: String,

    @SerializedName("relationshipId") // Antes: "relationship_id"
    val relationshipId: String,

    // --- CAMBIO AQUÍ ---
    @SerializedName("senderId") // Antes: "sender_id"
    val senderId: String,

    // --- CAMBIO AQUÍ ---
    @SerializedName("receiverId") // Antes: "receiver_id"
    val receiverId: String,

    @SerializedName("content")
    val content: MessageContentDto,

    @SerializedName("timestamp")
    val timestamp: String,

    @SerializedName("isRead")
    val isRead: Boolean,

    @SerializedName("messageType")
    val messageType: String
)