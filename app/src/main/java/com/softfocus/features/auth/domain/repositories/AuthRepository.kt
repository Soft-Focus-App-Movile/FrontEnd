package com.softfocus.features.auth.domain.repositories

import com.softfocus.features.auth.domain.models.User
import com.softfocus.features.auth.domain.models.UserType

/**
 * Repository interface for authentication operations.
 *
 * This interface defines the contract for authentication-related operations
 * without any implementation details. The actual implementation will be
 * provided in the data layer.
 *
 * All methods use Result<T> to handle success and failure cases in a functional way.
 */
interface AuthRepository {

    /**
     * Authenticates a user with email and password.
     *
     * @param email User's email address
     * @param password User's password
     * @return Result containing the authenticated User on success, or an exception on failure
     */
    suspend fun login(email: String, password: String): Result<User>

    /**
     * Registers a new user in the platform.
     *
     * @param email User's email address
     * @param password User's password
     * @param fullName User's full name
     * @param userType Type of user to register (GENERAL, PATIENT, PSYCHOLOGIST, ADMIN)
     * @return Result containing the newly registered User on success, or an exception on failure
     */
    suspend fun register(
        email: String,
        password: String,
        fullName: String,
        userType: UserType
    ): Result<User>

    /**
     * Authenticates a user using a social provider (Google, Facebook, etc.).
     *
     * @param provider Name of the social provider (e.g., "GOOGLE", "FACEBOOK")
     * @param token Authentication token from the social provider
     * @return Result containing the authenticated User on success, or an exception on failure
     */
    suspend fun socialLogin(provider: String, token: String): Result<User>
}