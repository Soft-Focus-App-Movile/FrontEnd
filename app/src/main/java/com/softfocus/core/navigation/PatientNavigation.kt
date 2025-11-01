package com.softfocus.core.navigation

import android.content.Context
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController

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
    // composable(Route.AssignedExercises.path) { ... }
}
