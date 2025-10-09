package com.softfocus.features.auth.presentation.register

import com.softfocus.features.auth.domain.models.User

/**
 * Represents the different states of the registration screen.
 *
 * This sealed class is used by RegisterViewModel to communicate UI state
 * changes to the RegisterScreen composable.
 */
sealed class RegisterState {

    /**
     * Initial idle state before any registration attempt.
     */
    data object Idle : RegisterState()

    /**
     * Loading state while the registration request is in progress.
     */
    data object Loading : RegisterState()

    /**
     * Success state with the newly registered user data.
     *
     * @property user The newly registered user
     */
    data class Success(val user: User) : RegisterState()

    /**
     * Error state with an error message.
     *
     * @property message The error message to display to the user
     */
    data class Error(val message: String) : RegisterState()
}
