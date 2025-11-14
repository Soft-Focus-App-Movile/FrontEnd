package com.softfocus.features.therapy.domain.usecases

import com.softfocus.features.therapy.domain.repositories.TherapyRepository

class SendChatMessageUseCase(
    private val repository: TherapyRepository
) {
    /**
     * Permite llamar a la clase como si fuera una función.
     * Devuelve el ID del mensaje enviado, como confirmaste.
     */
    suspend operator fun invoke(
        relationshipId: String,
        receiverId: String,
        content: String,
        messageType: String
    ): Result<String> {
        return repository.sendChatMessage(relationshipId, receiverId, content, messageType)
    }
}