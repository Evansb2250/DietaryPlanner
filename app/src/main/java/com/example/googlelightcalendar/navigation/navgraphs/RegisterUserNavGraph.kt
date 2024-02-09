package com.example.googlelightcalendar.navigation.navgraphs

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import com.example.googlelightcalendar.navigation.components.NavigationDestinations
import com.example.googlelightcalendar.screens.register.PhysicalDetailScreen
import com.example.googlelightcalendar.screens.register.RegisterGoalsScreen
import com.example.googlelightcalendar.ui.screens.register.RegistrationScreen

fun NavGraphBuilder.RegisterUserNavGraph() {
    navigation(
        route = NavigationDestinations.RegistrationPath.destination,
        startDestination = NavigationDestinations.RegisterScreen.destination
    ) {
        composable(
            route = NavigationDestinations.RegisterScreen.destination
        ) {
            RegistrationScreen()
        }

        composable(
            route = NavigationDestinations.RegisterPhysicalScreen.destination
        ) {
            PhysicalDetailScreen()
        }
        composable(
            route = NavigationDestinations.RegisterGoalsScreen.destination
        ) {
            RegisterGoalsScreen()
        }
    }
}
