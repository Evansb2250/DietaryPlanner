package com.example.googlelightcalendar.navigation.routes

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.example.googlelightcalendar.navigation.components.MainScreenNavigation
import com.example.googlelightcalendar.screens.food_diary.DiaryScreen
import com.example.googlelightcalendar.screens.profile.ProfileScreen
import com.example.googlelightcalendar.ui_components.ScreenUnavailable

fun NavGraphBuilder.MainScreenRoutes(
) {
    composable(
        route = MainScreenNavigation.Home.destination
    ) {
        ScreenUnavailable()
    }

    composable(
        route = MainScreenNavigation.Diary.destination
    ) {
        DiaryScreen()
    }

    composable(
        route = MainScreenNavigation.Calendar.destination
    ) {
        ScreenUnavailable()
    }

    composable(
        route = com.example.googlelightcalendar.navigation.components.ProfileRoutes.Profile.destination
    ) {
        ProfileScreen()
    }

    ProfileRoutes()
}