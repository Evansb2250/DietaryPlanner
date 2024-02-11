package com.example.googlelightcalendar.navigation.navgraphs

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.example.googlelightcalendar.navigation.components.destinations.ProfileDestinations
import com.example.googlelightcalendar.ui_components.ScreenUnavailable

fun NavGraphBuilder.ProfileNavGraph() {
    composable(
        route = ProfileDestinations.Account.destination
    ) {
        ScreenUnavailable()
    }

    composable(
        route = ProfileDestinations.NotificationScreen.destination
    ) {
        ScreenUnavailable()
    }

    composable(
        route = ProfileDestinations.Calendar.destination
    ) {
        ScreenUnavailable()
    }

    composable(
        route = ProfileDestinations.TOS.destination
    ) {
        ScreenUnavailable()
    }
}