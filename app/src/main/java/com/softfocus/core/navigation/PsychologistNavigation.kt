package com.softfocus.core.navigation

import android.content.Context
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController

/**
 * Psychologist navigation graph.
 * Contains routes specific to PSYCHOLOGIST users.
 *
 * Currently empty, but ready for future psychologist-specific features such as:
 * - My patients list
 * - Patient detail view
 * - Assign exercises to patients
 * - Session notes management
 * - Patient progress analytics
 * - Schedule management
 * - etc.
 */
fun NavGraphBuilder.psychologistNavigation(
    navController: NavHostController,
    context: Context
) {
    // Future psychologist-specific routes will be added here
    // Example:
    // composable(Route.MyPatients.path) { ... }
    // composable(Route.PatientDetail.path) { ... }
    // composable(Route.AssignExercise.path) { ... }
    // composable(Route.SessionNotes.path) { ... }
}
