package com.softfocus.features.auth.data.remote

import com.softfocus.features.auth.data.models.request.LoginRequestDto
import com.softfocus.features.auth.data.models.request.RegisterRequestDto
import com.softfocus.features.auth.data.models.request.SocialLoginRequestDto
import com.softfocus.features.auth.data.models.response.LoginResponseDto
import com.softfocus.features.auth.data.models.response.RegisterResponseDto
import retrofit2.http.Body
import retrofit2.http.POST

/**
 * Retrofit service interface for authentication-related API endpoints.
 *
 * This interface defines the HTTP operations for user authentication,
 * including email/password login, registration, and social login.
 *
 * Base URL is configured in the Retrofit instance provided by Hilt.
 */
interface AuthService {

    /**
     * Authenticates a user with email and password.
     *
     * Endpoint: POST /api/auth/login
     *
     * @param request Login credentials (email and password)
     * @return LoginResponseDto containing user data and authentication token
     */
    @POST("auth/login")
    suspend fun login(@Body request: LoginRequestDto): LoginResponseDto

    /**
     * Registers a new user in the platform.
     *
     * Endpoint: POST /api/auth/register
     *
     * @param request Registration data (email, password, userType)
     * @return RegisterResponseDto containing newly created user data and token
     */
    @POST("auth/register")
    suspend fun register(@Body request: RegisterRequestDto): RegisterResponseDto

    /**
     * Authenticates a user using a social provider (Google, Facebook, etc.).
     *
     * Endpoint: POST /api/auth/social-login
     *
     * @param request Social login data (provider name and token)
     * @return LoginResponseDto containing user data and authentication token
     */
    @POST("auth/social-login")
    suspend fun socialLogin(@Body request: SocialLoginRequestDto): LoginResponseDto
}
