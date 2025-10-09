package com.softfocus.features.auth.presentation.di

import com.softfocus.features.auth.data.di.DataModule.getAuthRepository
import com.softfocus.features.auth.presentation.login.LoginViewModel
import com.softfocus.features.auth.presentation.register.RegisterViewModel

object PresentationModule {

    fun getLoginViewModel(): LoginViewModel {
        return LoginViewModel(getAuthRepository())
    }

    fun getRegisterViewModel(): RegisterViewModel {
        return RegisterViewModel(getAuthRepository())
    }
}
