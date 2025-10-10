package com.softfocus.features.auth.presentation.register

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.softfocus.features.auth.domain.models.UserType
import com.softfocus.features.auth.domain.repositories.AuthRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

/**
 * ViewModel for user registration.
 * Handles two separate registration flows:
 * 1. General User: Simple registration with just firstName, lastName, email, password
 * 2. Psychologist: Complex registration with professional data and document uploads
 */
class RegisterViewModel(private val repository: AuthRepository) : ViewModel() {

    private val _email = MutableStateFlow("")
    val email: StateFlow<String> = _email

    private val _password = MutableStateFlow("")
    val password: StateFlow<String> = _password

    private val _confirmPassword = MutableStateFlow("")
    val confirmPassword: StateFlow<String> = _confirmPassword

    private val _userType = MutableStateFlow<UserType?>(null)
    val userType: StateFlow<UserType?> = _userType

    // Stores the registration result (userId, email)
    private val _registrationResult = MutableStateFlow<Pair<String, String>?>(null)
    val registrationResult: StateFlow<Pair<String, String>?> = _registrationResult

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage

    fun updateEmail(value: String) {
        _email.value = value
    }

    fun updatePassword(value: String) {
        _password.value = value
    }

    fun updateConfirmPassword(value: String) {
        _confirmPassword.value = value
    }

    fun updateUserType(value: UserType) {
        _userType.value = value
    }

    /**
     * Register a general user.
     * Flow: Register -> Auto-login in UI -> Navigate to General Home
     */
    fun registerGeneralUser(
        firstName: String,
        lastName: String,
        acceptsPrivacyPolicy: Boolean
    ) {
        if (password.value != confirmPassword.value) {
            _errorMessage.value = "Las contraseñas no coinciden"
            return
        }

        if (!acceptsPrivacyPolicy) {
            _errorMessage.value = "Debes aceptar la política de privacidad"
            return
        }

        viewModelScope.launch {
            _isLoading.value = true
            _errorMessage.value = null

            repository.registerGeneralUser(
                firstName = firstName,
                lastName = lastName,
                email = email.value,
                password = password.value,
                acceptsPrivacyPolicy = acceptsPrivacyPolicy
            ).onSuccess { result ->
                _registrationResult.value = result
                _isLoading.value = false
            }.onFailure { error ->
                _errorMessage.value = error.message ?: "Error desconocido al registrar"
                _isLoading.value = false
            }
        }
    }

    /**
     * Register a psychologist with all required documents.
     * Flow: Register with documents -> Navigate to Account Review Screen
     *
     * Note: Documents are uploaded directly in registration, no separate verification step needed
     */
    fun registerPsychologist(
        firstName: String,
        lastName: String,
        professionalLicense: String,
        yearsOfExperience: Int,
        collegiateRegion: String,
        university: String,
        graduationYear: Int,
        acceptsPrivacyPolicy: Boolean,
        licenseDocumentUri: String,
        diplomaDocumentUri: String,
        dniDocumentUri: String,
        specialties: String? = null, // comma-separated
        certificationDocumentUris: List<String>? = null
    ) {
        if (password.value != confirmPassword.value) {
            _errorMessage.value = "Las contraseñas no coinciden"
            return
        }

        if (!acceptsPrivacyPolicy) {
            _errorMessage.value = "Debes aceptar la política de privacidad"
            return
        }

        // Validate required fields
        if (professionalLicense.isBlank()) {
            _errorMessage.value = "El número de licencia es requerido"
            return
        }
        if (collegiateRegion.isBlank()) {
            _errorMessage.value = "La región de colegiatura es requerida"
            return
        }
        if (university.isBlank()) {
            _errorMessage.value = "La universidad es requerida"
            return
        }

        viewModelScope.launch {
            _isLoading.value = true
            _errorMessage.value = null

            repository.registerPsychologist(
                firstName = firstName,
                lastName = lastName,
                email = email.value,
                password = password.value,
                professionalLicense = professionalLicense,
                yearsOfExperience = yearsOfExperience,
                collegiateRegion = collegiateRegion,
                university = university,
                graduationYear = graduationYear,
                acceptsPrivacyPolicy = acceptsPrivacyPolicy,
                licenseDocumentUri = licenseDocumentUri,
                diplomaDocumentUri = diplomaDocumentUri,
                dniDocumentUri = dniDocumentUri,
                specialties = specialties,
                certificationDocumentUris = certificationDocumentUris
            ).onSuccess { result ->
                _registrationResult.value = result
                _isLoading.value = false
            }.onFailure { error ->
                _errorMessage.value = error.message ?: "Error desconocido al registrar"
                _isLoading.value = false
            }
        }
    }

    fun clearError() {
        _errorMessage.value = null
    }

    fun clearRegistrationResult() {
        _registrationResult.value = null
    }
}
