package com.softfocus.features.therapy.domain.repositories

import com.softfocus.features.therapy.domain.models.ChatMessage
import com.softfocus.features.therapy.domain.models.PatientDirectory
import com.softfocus.features.therapy.domain.models.PatientProfile
import com.softfocus.features.therapy.domain.models.TherapeuticRelationship
import com.softfocus.features.tracking.domain.model.CheckIn

interface TherapyRepository {

    suspend fun getMyRelationship(): Result<TherapeuticRelationship?>

    suspend fun connectWithPsychologist(connectionCode: String): Result<String>

    suspend fun getMyPatients(): Result<List<PatientDirectory>>

    suspend fun sendChatMessage(
        relationshipId: String,
        receiverId: String,
        content: String,
        messageType: String
    ): Result<String> // Devuelve el ID del mensaje

    suspend fun getChatHistory(
        relationshipId: String,
        page: Int,
        size: Int
    ): Result<List<ChatMessage>>

    suspend fun getLastReceivedMessage(): Result<ChatMessage?> // Puede ser nulo si no hay mensajes
    suspend fun getPatientProfile(patientId: String): Result<PatientProfile>

    suspend fun getPatientCheckIns(
        patientId: String,
        startDate: String? = null,
        endDate: String? = null,
        page: Int,
        pageSize: Int
    ): Result<List<CheckIn>>

    suspend fun disconnectRelationship(relationshipId: String): Result<Unit>

}
