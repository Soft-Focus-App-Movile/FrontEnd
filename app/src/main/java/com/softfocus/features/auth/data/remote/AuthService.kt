package com.softfocus.features.auth.data.remote

import com.softfocus.features.auth.data.models.request.LoginRequestDto
import com.softfocus.features.auth.data.models.request.RegisterGeneralUserRequestDto
import com.softfocus.features.auth.data.models.request.RegisterRequestDto
import com.softfocus.features.auth.data.models.request.SocialLoginRequestDto
import com.softfocus.features.auth.data.models.response.LoginResponseDto
import com.softfocus.features.auth.data.models.response.RegisterResponseDto
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.Body
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

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
     * Registers a new general user in the platform.
     *
     * Endpoint: POST /api/v1/auth/register/general
     *
     * @param request Registration data (firstName, lastName, email, password, acceptsPrivacyPolicy)
     * @return RegisterResponseDto containing newly created user data
     */
    @POST("auth/register/general")
    suspend fun registerGeneralUser(@Body request: RegisterGeneralUserRequestDto): RegisterResponseDto

    /**
     * Registers a new psychologist in the platform.
     * Requires multipart/form-data for document uploads.
     *
     * Endpoint: POST /api/v1/auth/register/psychologist
     *
     * @return RegisterResponseDto containing newly created user data (account pending verification)
     */
    @Multipart
    @POST("auth/register/psychologist")
    suspend fun registerPsychologist(
        @Part("firstName") firstName: RequestBody,
        @Part("lastName") lastName: RequestBody,
        @Part("email") email: RequestBody,
        @Part("password") password: RequestBody,
        @Part("professionalLicense") professionalLicense: RequestBody,
        @Part("yearsOfExperience") yearsOfExperience: RequestBody,
        @Part("collegiateRegion") collegiateRegion: RequestBody,
        @Part("university") university: RequestBody,
        @Part("graduationYear") graduationYear: RequestBody,
        @Part("acceptsPrivacyPolicy") acceptsPrivacyPolicy: RequestBody,
        @Part licenseDocument: MultipartBody.Part,
        @Part diplomaDocument: MultipartBody.Part,
        @Part dniDocument: MultipartBody.Part,
        @Part("specialties") specialties: RequestBody? = null,
        @Part certificationDocuments: List<MultipartBody.Part>? = null
    ): RegisterResponseDto

    /**
     * Legacy register endpoint - deprecated.
     * Use registerGeneralUser or registerPsychologist instead.
     *
     * @param request Registration data (email, password, userType)
     * @return RegisterResponseDto containing newly created user data and token
     */
    @Deprecated("Use registerGeneralUser or registerPsychologist instead")
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
