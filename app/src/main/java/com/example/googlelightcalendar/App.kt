package com.example.googlelightcalendar

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.example.googlelightcalendar.navigation.components.AuthNavManager
import com.example.googlelightcalendar.navigation.components.NavigationDestinations
import com.example.googlelightcalendar.navigation.navgraphs.OnAppStartRoute
import com.example.googlelightcalendar.navigation.navgraphs.RegisterUserNavGraph
import com.example.googlelightcalendar.ui.theme.appColor
import com.example.googlelightcalendar.ui_components.NavigatationStateObserver
import com.example.googlelightcalendar.ui_components.dialog.ExitAppDialog

@RequiresApi(Build.VERSION_CODES.P)
@Composable
fun App(
    navAuthManager: AuthNavManager,
    navControl: NavHostController = rememberNavController()
) {
    var showExitAppNotification by rememberSaveable {
        mutableStateOf(false)
    }

    navAuthManager.onBackPressCallback = {

        //TODO maybe add a when
        if (navControl.currentDestination?.route.equals(NavigationDestinations.loginScreen.destination)) {
            showExitAppNotification = true
        } else
            navControl.popBackStack()
    }

    if (showExitAppNotification) {
        ExitAppDialog(
            userResponse = {}
        )
    }

    NavigatationStateObserver(
        navigationState = navAuthManager.navigationState,
        navigateToDestination = { navDirection ->
            //clears the stack if we are navigating back to the LoginScreen
            if (navDirection.destination == NavigationDestinations.loginScreen.destination) {
                while (navControl.currentBackStack.value.isNotEmpty()) {
                    navControl.popBackStack()
                }
            }
            navControl.navigate(navDirection.destination) {
                this.launchSingleTop
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
        OnAppStartRoute()
        RegisterUserNavGraph()
    }
}
