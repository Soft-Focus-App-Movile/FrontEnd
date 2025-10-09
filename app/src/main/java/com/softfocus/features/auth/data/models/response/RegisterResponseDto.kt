package com.softfocus.features.auth.data.models.response

import com.softfocus.features.auth.domain.models.User
import com.softfocus.features.auth.domain.models.UserType

data class RegisterResponseDto(
    val token: String,
    val userId: String,
    val email: String,
    val fullName: String,
    val userType: String,
    val expiresAt: String
) {
    fun toDomain(): User {
        return User(
            id = userId,
            email = email,
            userType = UserType.valueOf(userType.uppercase()),
            isVerified = false, // Por defecto no verificado al registrarse
            token = token,
            fullName = fullName
        )
    }
}
