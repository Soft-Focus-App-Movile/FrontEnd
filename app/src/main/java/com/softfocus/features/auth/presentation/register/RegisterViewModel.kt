package com.softfocus.features.auth.presentation.register

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.softfocus.features.auth.domain.models.User
import com.softfocus.features.auth.domain.models.UserType
import com.softfocus.features.auth.domain.repositories.AuthRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class RegisterViewModel(private val repository: AuthRepository) : ViewModel() {

    private val _email = MutableStateFlow("")
    val email: StateFlow<String> = _email

    private val _password = MutableStateFlow("")
    val password: StateFlow<String> = _password

    private val _confirmPassword = MutableStateFlow("")
    val confirmPassword: StateFlow<String> = _confirmPassword

    private val _userType = MutableStateFlow<UserType?>(null)
    val userType: StateFlow<UserType?> = _userType

    private val _user = MutableStateFlow<User?>(null)
    val user: StateFlow<User?> = _user

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

    fun register(fullName: String) {
        if (password.value != confirmPassword.value) {
            _errorMessage.value = "Las contraseñas no coinciden"
            return
        }

        val currentUserType = userType.value
        if (currentUserType == null) {
            _errorMessage.value = "Por favor selecciona un tipo de usuario"
            return
        }

        viewModelScope.launch {
            _isLoading.value = true
            _errorMessage.value = null

            repository.register(email.value, password.value, fullName, currentUserType)
                .onSuccess { user ->
                    _user.value = user
                    _isLoading.value = false
                }
                .onFailure { error ->
                    _errorMessage.value = error.message ?: "Error desconocido"
                    _isLoading.value = false
                }
        }
    }

    fun clearError() {
        _errorMessage.value = null
    }
}
