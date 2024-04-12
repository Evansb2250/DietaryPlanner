package com.example.chooseu.navigation.navgraphs

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import com.example.chooseu.navigation.components.destinations.GeneralDestinations
import com.example.chooseu.screens.register.physical.PhysicalDetailScreen
import com.example.chooseu.ui.screens.register.goal_creation.RegisterGoalsScreen

fun NavGraphBuilder.RegisterUserNavGraph() {
    navigation(
        route = GeneralDestinations.RegistrationFlow.destination,
        startDestination = GeneralDestinations.RegisterDetailsFlow.destination
    ) {
        composable(
            route = GeneralDestinations.RegisterDetailsFlow.destination
        ) {
            PhysicalDetailScreen()
        }
        composable(
            route = GeneralDestinations.RegisterGoalsFlow.destination
        ) {
            RegisterGoalsScreen()
        }
    }
}
