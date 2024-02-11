package com.example.googlelightcalendar.navigation.navgraphs

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.example.googlelightcalendar.core.AccountViewModel
import com.example.googlelightcalendar.core.CalendarViewModel
import com.example.googlelightcalendar.core.NotificationViewModel
import com.example.googlelightcalendar.core.TOSViewModel
import com.example.googlelightcalendar.core.toolBarStates.ToolBarState
import com.example.googlelightcalendar.navigation.components.destinations.ProfileDestinations
import com.example.googlelightcalendar.ui.ui_components.ScreenUnavailable
import com.example.googlelightcalendar.ui.ui_components.custom_column.AppColumnContainer
import com.example.googlelightcalendar.ui.ui_components.toolbar.ChooseUToolBar

fun NavGraphBuilder.ProfileNavGraph() {
    composable(
        route = ProfileDestinations.Account.destination
    ) {
        val accountViewModel: AccountViewModel = hiltViewModel()
        Scaffold(
            topBar = {
                ChooseUToolBar(
                    toolBarState = ToolBarState.Navigated(
                        "Account"
                    ),
                    navigateBack = accountViewModel::onBackPress,
                    navigateToActionDestination = {}
                )
            }
        ) { it ->
            AppColumnContainer(
                modifier = Modifier.padding(it),
            ) {
                ScreenUnavailable()
            }
        }
    }

    composable(
        route = ProfileDestinations.NotificationScreen.destination
    ) {
        val notificationViewModel: NotificationViewModel = hiltViewModel()
        Scaffold(
            topBar = {
                ChooseUToolBar(
                    toolBarState = ToolBarState.Navigated(
                        "Notifications",
                    ),
                    navigateBack = notificationViewModel::onBackPress,
                    navigateToActionDestination = {}
                )
            }
        ) { it ->

            AppColumnContainer(
                modifier = Modifier.padding(it),
            ) {

                ScreenUnavailable()
            }
        }
    }

    composable(
        route = ProfileDestinations.Calendar.destination
    ) {
        val calendarViewModel: CalendarViewModel = hiltViewModel()

        Scaffold(
            topBar = {
                ChooseUToolBar(
                    toolBarState = ToolBarState.Navigated(
                        "Calendar"
                    ),
                    navigateBack = calendarViewModel::onBackPress,
                    navigateToActionDestination = {}
                )
            }
        ) { it ->
            AppColumnContainer(
                modifier = Modifier.padding(it),
            ) {

                ScreenUnavailable()
            }
        }
    }

    composable(
        route = ProfileDestinations.TOS.destination
    ) {
        val tosViewModel: TOSViewModel = hiltViewModel()

        Scaffold(
            topBar = {
                ChooseUToolBar(
                    toolBarState = ToolBarState.Navigated("Terms Of Service"),
                    navigateBack = tosViewModel::onBackPress,
                    navigateToActionDestination = {}
                )
            }
        ) { it ->
        AppColumnContainer(
            modifier = Modifier.padding(it),
            disableBackPress = false,
        ) {

                ScreenUnavailable()
            }
        }
    }
}