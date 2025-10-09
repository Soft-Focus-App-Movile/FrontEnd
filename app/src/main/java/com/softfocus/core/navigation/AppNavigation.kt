package com.softfocus.core.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.softfocus.features.auth.presentation.di.PresentationModule
import com.softfocus.features.auth.presentation.login.LoginScreen
import com.softfocus.features.auth.presentation.splash.SplashScreen

@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = Route.Splash.path
    ) {
        // Splash Screen
        composable(Route.Splash.path) {
            SplashScreen(
                onNavigateToLogin = {
                    navController.navigate(Route.Login.path) {
                        popUpTo(Route.Splash.path) { inclusive = true }
                    }
                },
                onNavigateToHome = {
                    navController.navigate(Route.Login.path) {
                        popUpTo(Route.Splash.path) { inclusive = true }
                    }
                }
            )
        }

        // Login Screen
        composable(Route.Login.path) {
            val viewModel = PresentationModule.getLoginViewModel()
            LoginScreen(
                viewModel = viewModel,
                onLoginSuccess = {
                    // TODO: Navigate based on verification status
                    navController.navigate(Route.Home.path) {
                        popUpTo(Route.Login.path) { inclusive = true }
                    }
                },
                onNavigateToRegister = {
                    navController.navigate(Route.Register.path)
                }
            )
        }

        // Register Screen
        composable(Route.Register.path) {
            // TODO: Implement RegisterScreen
        }

        // Home (placeholder)
        composable(Route.Home.path) {
            // TODO: Implement Home
        }
    }
}
