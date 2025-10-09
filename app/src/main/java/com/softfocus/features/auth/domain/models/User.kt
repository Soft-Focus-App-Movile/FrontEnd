package com.softfocus.features.auth.domain.models

/**
 * Represents a user in the Soft Focus platform.
 *
 * This is a domain entity containing only business logic data,
 * without any framework dependencies (Android, Retrofit, Room, etc.).
 *
 * @property id Unique identifier for the user
 * @property email User's email address
 * @property userType Type of user (GENERAL, PATIENT, PSYCHOLOGIST, ADMIN)
 * @property isVerified Whether the user's account has been verified by administrators
 * @property token Authentication token for API requests (nullable for pre-login states)
 * @property fullName User's full name (nullable until profile completion)
 */
data class User(
    val id: String,
    val email: String,
    val userType: UserType,
    val isVerified: Boolean = false,
    val token: String? = null,
    val fullName: String? = null
)