package com.example.chooseu.navigation.navgraphs

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import com.example.chooseu.navigation.components.destinations.GeneralDestinations
import com.example.chooseu.screens.register.physical.PhysicalDetailScreen
import com.example.chooseu.ui.screens.register.goal_creation.RegisterGoalsScreen

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
