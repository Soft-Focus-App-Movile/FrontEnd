package com.softfocus.features.therapy.data.models.response

import com.google.gson.annotations.SerializedName

/**
 * DTO para un mensaje de chat (usado en History y LastReceived)
 */
data class TherapyChatResponseDto(
    @SerializedName("id")
    val id: String,

    @SerializedName("relationship_id")
    val relationshipId: String,

    @SerializedName("sender_id")
    val senderId: String,

    @SerializedName("receiver_id")
    val receiverId: String,

    @SerializedName("content")
    val content: MessageContentDto,

    @SerializedName("timestamp")
    val timestamp: String,

    @SerializedName("is_read")
    val isRead: Boolean,

    @SerializedName("message_type")
    val messageType: String
)