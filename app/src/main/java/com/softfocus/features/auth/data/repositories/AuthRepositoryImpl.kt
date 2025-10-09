package com.softfocus.features.auth.data.repositories

import com.softfocus.features.auth.data.models.request.LoginRequestDto
import com.softfocus.features.auth.data.models.request.RegisterRequestDto
import com.softfocus.features.auth.data.models.request.SocialLoginRequestDto
import com.softfocus.features.auth.data.remote.AuthService
import com.softfocus.features.auth.domain.models.User
import com.softfocus.features.auth.domain.models.UserType
import com.softfocus.features.auth.domain.repositories.AuthRepository

/**
 * Implementation of AuthRepository interface.
 *
 * This class provides the concrete implementation for authentication operations,
 * handling HTTP requests through Retrofit and mapping DTOs to domain models.
 *
 * @property authService Retrofit service for making API calls
 */
class AuthRepositoryImpl(
    private val authService: AuthService
) : AuthRepository {

    /**
     * Authenticates a user with email and password.
     *
     * @param email User's email address
     * @param password User's password
     * @return Result containing the authenticated User on success, or an exception on failure
     */
    override suspend fun login(email: String, password: String): Result<User> {
        return try {
            val request = LoginRequestDto(email = email, password = password)
            val response = authService.login(request)
            Result.success(response.toDomain())
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    /**
     * Registers a new user in the platform.
     *
     * @param email User's email address
     * @param password User's password
     * @param fullName User's full name
     * @param userType Type of user to register (GENERAL, PATIENT, PSYCHOLOGIST, ADMIN)
     * @return Result containing the newly registered User on success, or an exception on failure
     */
    override suspend fun register(
        email: String,
        password: String,
        fullName: String,
        userType: UserType
    ): Result<User> {
        return try {
            val request = RegisterRequestDto(
                email = email,
                password = password,
                fullName = fullName,
                userType = userType.name
            )
            val response = authService.register(request)
            Result.success(response.toDomain())
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    /**
     * Authenticates a user using a social provider (Google, Facebook, etc.).
     *
     * @param provider Name of the social provider (e.g., "GOOGLE", "FACEBOOK")
     * @param token Authentication token from the social provider
     * @return Result containing the authenticated User on success, or an exception on failure
     */
    override suspend fun socialLogin(provider: String, token: String): Result<User> {
        return try {
            val request = SocialLoginRequestDto(provider = provider, token = token)
            val response = authService.socialLogin(request)
            Result.success(response.toDomain())
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
