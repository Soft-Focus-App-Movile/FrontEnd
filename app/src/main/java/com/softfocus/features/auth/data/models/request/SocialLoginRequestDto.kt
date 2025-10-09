package com.softfocus.features.auth.data.models.request

data class SocialLoginRequestDto(
    val provider: String,
    val token: String
)
