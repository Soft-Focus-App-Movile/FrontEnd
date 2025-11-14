package com.softfocus.features.therapy.domain.usecases

import com.softfocus.features.therapy.domain.repositories.TherapyRepository

class GetRelationshipWithPatientUseCase(
    private val repository: TherapyRepository
) {
    suspend operator fun invoke(patientId: String): Result<String> {
        return repository.getRelationshipWithPatient(patientId)
    }
}