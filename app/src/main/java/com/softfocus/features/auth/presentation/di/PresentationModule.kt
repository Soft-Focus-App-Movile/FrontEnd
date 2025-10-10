package com.softfocus.features.auth.presentation.di

import android.content.Context
import com.softfocus.features.auth.data.di.DataModule.getAuthRepository
import com.softfocus.features.auth.presentation.login.LoginViewModel
import com.softfocus.features.auth.presentation.register.RegisterViewModel

object PresentationModule {

    fun getLoginViewModel(context: Context): LoginViewModel {
        return LoginViewModel(getAuthRepository(context))
    }

    fun getRegisterViewModel(context: Context): RegisterViewModel {
        return RegisterViewModel(getAuthRepository(context))
    }
}
