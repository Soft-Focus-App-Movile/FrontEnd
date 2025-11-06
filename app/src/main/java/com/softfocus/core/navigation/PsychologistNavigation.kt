package com.softfocus.core.navigation

import android.content.Context
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import com.softfocus.features.profile.presentation.psychologist.PsychologistProfileScreen
import com.softfocus.features.profile.presentation.psychologist.EditPersonalInfoScreen
import com.softfocus.features.profile.presentation.psychologist.ProfessionalDataScreen


/**
 * Psychologist navigation graph.
 * Contains routes specific to PSYCHOLOGIST users.
 */
fun NavGraphBuilder.psychologistNavigation(
    navController: NavHostController,
    context: Context
) {

    // Future psychologist-specific routes will be added here
    // Example:
    // composable(Route.MyPatients.path) { ... }
    // composable(Route.PatientDetail.path) { ... }
    // composable(Route.SessionNotes.path) { ... }

    // Psychologist Profile Screen
    composable(Route.PsychologistProfile.path) {
        PsychologistProfileScreen(
            onNavigateToEditProfile = {
                navController.navigate(Route.PsychologistEditProfile.path)
            },
            onNavigateToInvitationCode = {
                navController.navigate(Route.InvitationCode.path)
            },
            onNavigateToNotifications = {
                navController.navigate(Route.Notifications.path)
            },
            onNavigateToPlan = {
                navController.navigate(Route.PsychologistPlan.path)
            },
            onNavigateToStats = {
                navController.navigate(Route.PsychologistStats.path)
            },
            onNavigateToProfessionalData = {
                navController.navigate(Route.ProfessionalData.path)
            },
            onNavigateBack = {
                navController.popBackStack()
            }
        )
    }

    // Psychologist Edit Profile Screen
    composable(Route.PsychologistEditProfile.path) {
        EditPersonalInfoScreen(
            onNavigateBack = {
                navController.popBackStack()
            }
        )
    }

    // Professional Data Screen
    composable(Route.ProfessionalData.path) {
        ProfessionalDataScreen(
            onNavigateBack = {
                navController.popBackStack()
            }
        )
    }

}
