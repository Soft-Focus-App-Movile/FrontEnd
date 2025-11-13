package com.softfocus.features.therapy.domain.usecases

import com.softfocus.features.therapy.domain.models.ChatMessage
import com.softfocus.features.therapy.domain.repositories.TherapyRepository

class GetChatHistoryUseCase(
    private val repository: TherapyRepository
) {
    // Permite llamar a la clase como si fuera una función
    suspend operator fun invoke(
        relationshipId: String,
        page: Int,
        size: Int
    ): Result<List<ChatMessage>> {
        return repository.getChatHistory(relationshipId, page, size)
    }
}