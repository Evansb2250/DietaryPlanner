package com.example.googlelightcalendar

import android.os.Build
import androidx.activity.compose.BackHandler
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.example.googlelightcalendar.core.app.AppViewModel
import com.example.googlelightcalendar.navigation.components.NavigationDestinations
import com.example.googlelightcalendar.navigation.navgraphs.OnAppStartNavGraph
import com.example.googlelightcalendar.navigation.navgraphs.RegisterUserNavGraph
import com.example.googlelightcalendar.ui.theme.appColor
import com.example.googlelightcalendar.ui_components.NavManagerStateObserver
import com.example.googlelightcalendar.ui_components.dialog.ExitAppDialog

@RequiresApi(Build.VERSION_CODES.P)
@Composable
fun App(
    viewModel: AppViewModel = hiltViewModel(),
    navControl: NavHostController = rememberNavController(),
    closeApp: () -> Unit,
) {


    BackHandler {
        viewModel.showExitDialog()
    }

    if (viewModel.showExitAppNotification) {
        ExitAppDialog(
            userResponse = {
                if (it) {
                    closeApp()
                } else {
                    viewModel.closeExitDialog()
                }
            }
        )
    }

    NavManagerStateObserver(
        navigationState = viewModel.navigationManager.navigationState,
        navigateToDestination = { navDirection ->
            //clears the stack if we are navigating back to the LoginScreen
            if (navDirection.destination == NavigationDestinations.OnAppStartUp.destination) {
                navControl.navigate(NavigationDestinations.OnAppStartUp.destination) {
                    popUpTo(NavigationDestinations.OnAppStartUp.destination) {
                        inclusive = true
                    }
                }
            } else {
                navControl.navigate(navDirection.destination) {
                    this.launchSingleTop
                }
            }
        },
    )

    NavHost(
        modifier = Modifier
            .background(
                // to prevent flicker while transitioning
                color = appColor
            ),
        navController = navControl,
        startDestination = NavigationDestinations.OnAppStartUp.destination,
    ) {
        OnAppStartNavGraph()
        RegisterUserNavGraph()
    }
}
