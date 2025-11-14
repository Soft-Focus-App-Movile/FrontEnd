package com.softfocus.features.therapy.domain.usecases

import com.softfocus.features.therapy.domain.models.ChatMessage
import com.softfocus.features.therapy.domain.repositories.TherapyRepository

class GetLastReceivedMessageUseCase(
    private val repository: TherapyRepository
) {
    // Permite llamar a la clase como si fuera una función
    suspend operator fun invoke(): Result<ChatMessage?> {
        return repository.getLastReceivedMessage()
    }
}