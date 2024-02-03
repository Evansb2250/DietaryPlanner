package com.example.googlelightcalendar.navigation.routes

import androidx.compose.material3.Text
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.example.googlelightcalendar.navigation.components.MainScreenNavigations
import com.example.googlelightcalendar.ui_components.ScreenUnavailable

fun NavGraphBuilder.ProfileRoutes() {
    composable(
        route = com.example.googlelightcalendar.navigation.components.ProfileRoutes.Account.destination
    ) {
        ScreenUnavailable()
    }

    composable(
        route = com.example.googlelightcalendar.navigation.components.ProfileRoutes.Notifications.destination
    ) {
        ScreenUnavailable()
    }

    composable(
        route = com.example.googlelightcalendar.navigation.components.ProfileRoutes.Calendar.destination
    ) {
        ScreenUnavailable()
    }

    composable(
        route = com.example.googlelightcalendar.navigation.components.ProfileRoutes.TOS.destination
    ) {
        ScreenUnavailable()
    }

    composable(
        route = MainScreenNavigations.NotificationScreen.destination
    ) {
        Text(text = "Notification")
    }
}