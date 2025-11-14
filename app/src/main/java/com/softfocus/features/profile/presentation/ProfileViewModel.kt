package com.softfocus.features.profile.presentation

import android.content.Context
import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.softfocus.core.data.local.LocalUserDataSource
import com.softfocus.core.data.local.UserSession
import com.softfocus.features.auth.domain.models.User
import com.softfocus.features.profile.domain.models.AssignedPsychologist
import com.softfocus.features.profile.domain.repositories.ProfileRepository
import com.softfocus.features.therapy.domain.repositories.TherapyRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val profileRepository: ProfileRepository,
    private val therapyRepository: TherapyRepository,
    private val userSession: UserSession,
    @ApplicationContext private val context: Context
) : ViewModel() {

    private val _uiState = MutableStateFlow<ProfileUiState>(ProfileUiState.Loading)
    val uiState: StateFlow<ProfileUiState> = _uiState.asStateFlow()

    private val _user = MutableStateFlow<User?>(null)
    val user: StateFlow<User?> = _user.asStateFlow()

    private val _assignedPsychologist = MutableStateFlow<AssignedPsychologist?>(null)
    val assignedPsychologist: StateFlow<AssignedPsychologist?> = _assignedPsychologist.asStateFlow()

    private val _psychologistLoadState = MutableStateFlow<PsychologistLoadState>(PsychologistLoadState.Loading)
    val psychologistLoadState: StateFlow<PsychologistLoadState> = _psychologistLoadState.asStateFlow()

    private val _relationshipId = MutableStateFlow<String?>(null)
    val relationshipId: StateFlow<String?> = _relationshipId.asStateFlow()

    init {
        loadProfile()
    }

    fun loadProfile() {
        viewModelScope.launch {
            _uiState.value = ProfileUiState.Loading

            val cachedUser = userSession.getUser()
            if (cachedUser != null) {
                _user.value = cachedUser
            } else {
                _uiState.value = ProfileUiState.Error("Sesión expirada. Por favor, inicia sesión nuevamente.")
                return@launch
            }

            if (cachedUser.token.isNullOrEmpty()) {
                _uiState.value = ProfileUiState.Error("Token no válido. Por favor, inicia sesión nuevamente.")
                return@launch
            }

            profileRepository.getProfile()
                .onSuccess { user ->
                    _user.value = user
                    _uiState.value = ProfileUiState.Success

                    // Load assigned psychologist if user is a patient
                    loadAssignedPsychologist()
                }
                .onFailure { error ->
                    _uiState.value = ProfileUiState.Error(
                        error.message ?: "Error al cargar perfil"
                    )
                }
        }
    }

    private fun loadAssignedPsychologist() {
        viewModelScope.launch {
            _psychologistLoadState.value = PsychologistLoadState.Loading

            // Primero obtener la relación terapéutica para tener el relationshipId
            therapyRepository.getMyRelationship()
                .onSuccess { relationship ->
                    if (relationship != null && relationship.isActive) {
                        // Guardar el relationshipId
                        _relationshipId.value = relationship.id

                        // Luego cargar datos del psicólogo
                        viewModelScope.launch {
                            profileRepository.getAssignedPsychologist()
                                .onSuccess { psychologist ->
                                    if (psychologist != null) {
                                        _assignedPsychologist.value = psychologist
                                        _psychologistLoadState.value = PsychologistLoadState.Success
                                    } else {
                                        _psychologistLoadState.value = PsychologistLoadState.NoTherapist
                                    }
                                }
                                .onFailure { error ->
                                    _psychologistLoadState.value = PsychologistLoadState.Error(
                                        error.message ?: "Error al cargar información del terapeuta"
                                    )
                                }
                        }
                    } else {
                        _relationshipId.value = null
                        _psychologistLoadState.value = PsychologistLoadState.NoTherapist
                    }
                }
                .onFailure { error ->
                    _relationshipId.value = null
                    _psychologistLoadState.value = PsychologistLoadState.Error(
                        error.message ?: "Error al cargar información del terapeuta"
                    )
                }
        }
    }

    fun updateProfile(
        firstName: String?,
        lastName: String?,
        dateOfBirth: String?,
        gender: String?,
        phone: String?,
        bio: String?,
        country: String?,
        city: String?,
        interests: List<String>?,
        mentalHealthGoals: List<String>?,
        emailNotifications: Boolean?,
        pushNotifications: Boolean?,
        isProfilePublic: Boolean?,
        profileImageUri: Uri? = null
    ) {
        viewModelScope.launch {
            _uiState.value = ProfileUiState.Loading

            profileRepository.updateProfile(
                firstName = firstName,
                lastName = lastName,
                dateOfBirth = dateOfBirth,
                gender = gender,
                phone = phone,
                bio = bio,
                country = country,
                city = city,
                interests = interests,
                mentalHealthGoals = mentalHealthGoals,
                emailNotifications = emailNotifications,
                pushNotifications = pushNotifications,
                isProfilePublic = isProfilePublic,
                profileImageUri = profileImageUri
            )
                .onSuccess { user ->
                    _user.value = user
                    _uiState.value = ProfileUiState.UpdateSuccess
                }
                .onFailure { error ->
                    _uiState.value = ProfileUiState.Error(
                        error.message ?: "Error al actualizar perfil"
                    )
                }
        }
    }

    fun updateProfessionalProfile(
        professionalBio: String?,
        isAcceptingNewPatients: Boolean?,
        maxPatientsCapacity: Int?,
        targetAudience: List<String>?,
        languages: List<String>?,
        businessName: String?,
        businessAddress: String?,
        bankAccount: String?,
        paymentMethods: String?,
        isProfileVisibleInDirectory: Boolean?,
        allowsDirectMessages: Boolean?
    ) {
        viewModelScope.launch {
            android.util.Log.d("ProfileViewModel", "updateProfessionalProfile called")
            android.util.Log.d("ProfileViewModel", "Bio: $professionalBio, Languages: $languages, TargetAudience: $targetAudience")

            _uiState.value = ProfileUiState.Loading

            val result = profileRepository.updateProfessionalProfile(
                professionalBio = professionalBio,
                isAcceptingNewPatients = isAcceptingNewPatients,
                maxPatientsCapacity = maxPatientsCapacity,
                targetAudience = targetAudience,
                languages = languages,
                businessName = businessName,
                businessAddress = businessAddress,
                bankAccount = bankAccount,
                paymentMethods = paymentMethods,
                isProfileVisibleInDirectory = isProfileVisibleInDirectory,
                allowsDirectMessages = allowsDirectMessages
            )

            android.util.Log.d("ProfileViewModel", "Result: ${result.isSuccess}, ${result.isFailure}")

            result.onSuccess {
                    android.util.Log.d("ProfileViewModel", "Professional profile updated successfully")
                    // Reload profile to get updated data
                    loadProfile()
                    _uiState.value = ProfileUiState.UpdateSuccess
                }
                .onFailure { error ->
                    android.util.Log.e("ProfileViewModel", "Error updating professional profile", error)
                    _uiState.value = ProfileUiState.Error(
                        error.message ?: "Error al actualizar perfil profesional"
                    )
                }
        }
    }

    fun disconnectPsychologist(onSuccess: () -> Unit) {
        viewModelScope.launch {
            val currentRelationshipId = _relationshipId.value
            if (currentRelationshipId == null) {
                _uiState.value = ProfileUiState.Error("No hay una relación terapéutica activa")
                return@launch
            }

            _uiState.value = ProfileUiState.Loading

            therapyRepository.disconnectRelationship(currentRelationshipId)
                .onSuccess {
                    // Limpiar el estado de la relación terapéutica en SharedPreferences
                    val localUserDataSource = LocalUserDataSource(context)
                    localUserDataSource.clearTherapeuticRelationship()

                    // Actualizar estado del psicólogo a NoTherapist
                    _assignedPsychologist.value = null
                    _relationshipId.value = null
                    _psychologistLoadState.value = PsychologistLoadState.NoTherapist
                    _uiState.value = ProfileUiState.Success

                    // Llamar callback para navegación
                    onSuccess()
                }
                .onFailure { error ->
                    _uiState.value = ProfileUiState.Error(
                        error.message ?: "Error al desvincular terapeuta"
                    )
                }
        }
    }
}

sealed class ProfileUiState {
    object Loading : ProfileUiState()
    object Success : ProfileUiState()
    object UpdateSuccess : ProfileUiState()
    data class Error(val message: String) : ProfileUiState()
}

sealed class PsychologistLoadState {
    object Loading : PsychologistLoadState()
    object Success : PsychologistLoadState()
    object NoTherapist : PsychologistLoadState()
    data class Error(val message: String) : PsychologistLoadState()
}
