package com.example.googlelightcalendar.navigation.navgraphs

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import com.example.googlelightcalendar.navigation.components.destinations.GeneralDestinations
import com.example.googlelightcalendar.screens.register.physical.PhysicalDetailScreen
import com.example.googlelightcalendar.ui.screens.register.goal_creation.RegisterGoalsScreen

fun NavGraphBuilder.RegisterUserNavGraph() {
    navigation(
        route = GeneralDestinations.RegistrationDestinations.destination,
        startDestination = GeneralDestinations.RegisterDetailsDestination.destination
    ) {
        composable(
            route = GeneralDestinations.RegisterDetailsDestination.destination
        ) {
            PhysicalDetailScreen()
        }
        composable(
            route = GeneralDestinations.RegisterGoalsDestination.destination
        ) {
            RegisterGoalsScreen()
        }
    }
}
