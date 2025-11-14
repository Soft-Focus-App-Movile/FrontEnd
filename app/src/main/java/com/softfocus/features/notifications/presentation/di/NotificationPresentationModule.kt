package com.softfocus.features.notifications.presentation.di

object NotificationPresentationModule {

    private var authToken: String = ""

    fun clearAuthToken() {
        authToken = ""
    }

}