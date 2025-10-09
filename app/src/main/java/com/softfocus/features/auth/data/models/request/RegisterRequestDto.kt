package com.softfocus.features.auth.data.models.request

data class RegisterRequestDto(
    val email: String,
    val password: String,
    val fullName: String,
    val userType: String
)
