package com.softfocus.core.networking

object ApiConstants {
    const val BASE_URL = "http://localhost:5007/api/v1/"

    // Auth endpoints
    object Auth {
        const val REGISTER = "auth/register"
        const val LOGIN = "auth/login"
        const val OAUTH = "auth/oauth"
        const val FORGOT_PASSWORD = "auth/forgot-password"
        const val RESET_PASSWORD = "auth/reset-password"
    }

    // User endpoints
    object Users {
        const val PROFILE = "users/profile"
    }

    // AI endpoints
    object AI {
        const val CHAT_MESSAGE = "ai/chat/message"
        const val CHAT_USAGE = "ai/chat/usage"
        const val EMOTION_ANALYZE = "ai/emotion/analyze"
        const val EMOTION_USAGE = "ai/emotion/usage"
    }
}
