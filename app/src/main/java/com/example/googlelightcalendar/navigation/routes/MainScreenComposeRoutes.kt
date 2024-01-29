package com.example.googlelightcalendar.navigation.routes

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.example.googlelightcalendar.core.toolBarStates.ToolBarState
import com.example.googlelightcalendar.navigation.components.MainScreenNavManager
import com.example.googlelightcalendar.navigation.components.MainScreenNavigation
import com.example.googlelightcalendar.screens.food_diary.DiaryScreen
import com.example.googlelightcalendar.screens.profile.ProfileScreen
import com.example.googlelightcalendar.ui.theme.appColor
import com.example.googlelightcalendar.ui_components.toolbar.ChooseUToolBar

fun NavGraphBuilder.MainScreenRoutes(
    navigationManager: MainScreenNavManager,
) {
    composable(
        route = MainScreenNavigation.Home.destination
    ) {
        BackHandler {
            navigationManager.onBackPress()
        }
        Scaffold(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    color = appColor,
                ),
            topBar = {
                ChooseUToolBar(
                    toolBarState = ToolBarState.Home,
                    navigateBack = {},
                    navigateToActionDestination = {},
                )
            }
        ) { it->
            Box(
                modifier = Modifier
                    .background(
                        color = appColor,
                    ),
                contentAlignment = Alignment.TopCenter
            ){}
        }

    }

    composable(
        route = MainScreenNavigation.Diary.destination
    ) {
        DiaryScreen()
    }

    composable(
        route = MainScreenNavigation.Calendar.destination
    ) {
        BackHandler {
            navigationManager.onBackPress()
        }
        Scaffold(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    color = appColor,
                ),
            topBar = {
            }
        ){ it->
            Box(
                modifier = Modifier
                    .background(
                        color = appColor,
                    )
                    .fillMaxSize(),
                contentAlignment = Alignment.TopCenter
            ){

            }

        }
    }

    composable(
        route = com.example.googlelightcalendar.navigation.components.ProfileRoutes.Profile.destination
    ) {
        ProfileScreen()
    }

    ProfileRoutes()
}