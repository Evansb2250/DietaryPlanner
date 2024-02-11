package com.example.googlelightcalendar.navigation.navgraphs

import androidx.activity.compose.BackHandler
import androidx.compose.material3.Scaffold
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.example.googlelightcalendar.core.toolBarStates.ToolBarState
import com.example.googlelightcalendar.navigation.components.destinations.BottomNavBarDestinations
import com.example.googlelightcalendar.navigation.components.destinations.GeneralDestinations
import com.example.googlelightcalendar.ui.screens.food_diary.DiaryScreen
import com.example.googlelightcalendar.ui.screens.food_search.FoodSearchScreen
import com.example.googlelightcalendar.ui.screens.profile.ProfileScreen
import com.example.googlelightcalendar.ui_components.ScreenUnavailable
import com.example.googlelightcalendar.ui_components.toolbar.ChooseUToolBar

fun NavGraphBuilder.MainScreenRoutes(
) {
    composable(
        route = BottomNavBarDestinations.Home.destination
    ) {
        Scaffold(
            topBar = {
                ChooseUToolBar(
                    toolBarState = ToolBarState.Home(),
                    navigateBack = { /*TODO*/ },
                    navigateToActionDestination = {}
                )
            }
        ) { it ->
            BackHandler {

            }

            ScreenUnavailable()
        }
    }

    composable(
        route = BottomNavBarDestinations.Diary.destination
    ) {
        DiaryScreen()
    }

    composable(
        route = BottomNavBarDestinations.Calendar.destination
    ) {
        Scaffold(
            topBar = {
                ChooseUToolBar(
                    toolBarState = ToolBarState.Home(),
                    navigateBack = { /*TODO*/ },
                    navigateToActionDestination = {}
                )
            }
        ) { it ->
            BackHandler {

            }

            ScreenUnavailable()
        }
    }

    composable(
        route = BottomNavBarDestinations.Profile.destination
    ) {
        ProfileScreen()
    }

    ProfileNavGraph()

    composable(
        route = GeneralDestinations.FoodSearchDestination.destination
    ) {
        val title = it.arguments?.getString("foodType") ?: "cant find"
        FoodSearchScreen(
            title = title,
        )
    }
}