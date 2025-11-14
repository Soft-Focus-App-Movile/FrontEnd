package com.softfocus.features.therapy.presentation.patient.psychologistprofile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.softfocus.core.data.local.LocalUserDataSource
import com.softfocus.core.data.local.UserSession
import com.softfocus.features.profile.presentation.ProfileUiState
import com.softfocus.features.profile.presentation.PsychologistLoadState
import com.softfocus.features.search.domain.repositories.SearchRepository
import com.softfocus.features.therapy.domain.models.ChatMessage
import com.softfocus.features.therapy.domain.repositories.TherapyRepository
import com.softfocus.features.therapy.domain.usecases.GetMyRelationshipUseCase
import com.softfocus.features.therapy.presentation.patient.PsychologistChatUiState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

data class PsychologistSummaryState(
    val isLoading: Boolean = true,
    val psychologistName: String = "Cargando...",
    val profilePhotoUrl: String = "",
    val bio: String = "Soy $psychologistName",
    val university: String = "Universidad",
    val degree: String = "Título",
    val email: String = " ",
    val phoneNumber: String = " ",
    val specialties: List<String> = emptyList()
)

sealed class PsyProfileUiState {
    object Loading : PsyProfileUiState()
    object Success : PsyProfileUiState()
    object UpdateSuccess : PsyProfileUiState()
    data class Error(val message: String) : PsyProfileUiState()
}

sealed class PsyProfileLoadState {
    object Loading : PsyProfileLoadState()
    object Success : PsyProfileLoadState()
    object NoTherapist : PsyProfileLoadState()
    data class Error(val message: String) : PsyProfileLoadState()
}

data class PsyChatProfileUiState(
    val isLoading: Boolean = true,
    val error: String? = null
)


class PsyChatProfileViewModel(
    userSession: UserSession,
    private val getMyRelationshipUseCase: GetMyRelationshipUseCase,
    private val searchRepository: SearchRepository
): ViewModel() {
    private val _summaryState = MutableStateFlow(PsychologistSummaryState())
    val summaryState: StateFlow<PsychologistSummaryState> = _summaryState.asStateFlow()
    private val _uiState = MutableStateFlow(PsyChatProfileUiState())
    val uiState: StateFlow<PsyChatProfileUiState> = _uiState.asStateFlow()

    private var patientId: String? = null
    private var psychologistId: String? = null
    private var relationshipId: String? = null

    init {

        patientId = userSession.getUser()?.id

        if (patientId == null) {
            _uiState.update { it.copy(isLoading = false, error = "Error: No se pudo obtener el ID del paciente.") }
        } else {
            loadPsychologistDetails()
        }
    }

    private fun loadPsychologistDetails() {
        viewModelScope.launch(Dispatchers.IO) {

            val relationshipResult = getMyRelationshipUseCase()
            val relationship = relationshipResult.getOrThrow()
            relationshipId = relationship?.id
            psychologistId = relationship?.psychologistId
            // Llama al repositorio para obtener los detalles
            val result = searchRepository.getPsychologistById("$psychologistId")

            result.onSuccess { psychologist ->
                // Actualiza el state con la información completa
                withContext(Dispatchers.Main) {
                    _summaryState.update {
                        it.copy(
                            psychologistName = psychologist.fullName,
                            profilePhotoUrl = "${psychologist.profileImageUrl}",
                            bio = "${psychologist.professionalBio}",
                            university = "${psychologist.university}",
                            degree = "${psychologist.degree}",
                            email = "${psychologist.email}",
                            phoneNumber = "${psychologist.phone}",
                            specialties = psychologist.specialties
                        )
                    }
                }
            }.onFailure {
                // Opcional: manejar el error.
                // No es crítico porque ya tenemos el nombre básico de la relación.
            }
        }
    }
}