package com.softfocus.features.auth.presentation.login

import com.softfocus.features.auth.domain.models.User

/**
 * Represents the different states of the login screen.
 *
 * This sealed class is used by LoginViewModel to communicate UI state
 * changes to the LoginScreen composable.
 */
sealed class LoginState {

    /**
     * Initial idle state before any login attempt.
     */
    data object Idle : LoginState()

    /**
     * Loading state while the login request is in progress.
     */
    data object Loading : LoginState()

    /**
     * Success state with the authenticated user data.
     *
     * @property user The authenticated user
     */
    data class Success(val user: User) : LoginState()

    /**
     * Error state with an error message.
     *
     * @property message The error message to display to the user
     */
    data class Error(val message: String) : LoginState()
}
