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

/**
 * Patient navigation graph.
 * Contains routes specific to PATIENT users (users with a psychologist assigned).
 *
 * Currently empty, but ready for future patient-specific features such as:
 * - Therapy sessions list
 * - My psychologist profile
 * - Assigned exercises
 * - Session notes
 * - Progress tracking
 * - etc.
 */
fun NavGraphBuilder.patientNavigation(
    navController: NavHostController,
    context: Context
) {
    // Future patient-specific routes will be added here
    // Example:
    // composable(Route.TherapySessions.path) { ... }
    // composable(Route.MyPsychologist.path) { ... }
}
