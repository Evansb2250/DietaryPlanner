package com.example.chooseu

import android.os.Build
import androidx.activity.compose.BackHandler
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.example.chooseu.core.app.AppViewModel
import com.example.chooseu.core.on_startup.state.LastSignInState
import com.example.chooseu.navigation.components.destinations.GeneralDestinations
import com.example.chooseu.navigation.navgraphs.OnAppStartNavGraph
import com.example.chooseu.navigation.navgraphs.RegisterUserNavGraph
import com.example.chooseu.ui.theme.appColor
import com.example.chooseu.ui.ui_components.NavManagerStateObserver
import com.example.chooseu.ui.ui_components.dialog.ExitAppDialog

@RequiresApi(Build.VERSION_CODES.P)
@Composable
fun App(
    lastSignInState: LastSignInState,
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
            if (navDirection.destination == GeneralDestinations.AuthentificationFlow.destination) {

                navControl.navigate(GeneralDestinations.AuthentificationFlow.destination) {

                    popUpTo(GeneralDestinations.AuthentificationFlow.destination) {
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



    LaunchedEffect(key1 = Unit) {
        if(lastSignInState is LastSignInState.AlreadyLoggedIn){
            navControl.navigate(
                GeneralDestinations.MainScreenFlow.destination.replace(
                    "{userId}",
                    lastSignInState.userId
                )
            )
        }
    }


    NavHost(
        modifier = Modifier
            .background(
                // to prevent flicker while transitioning
                color = appColor
            ),
        navController = navControl,
        startDestination = GeneralDestinations.AuthentificationFlow.destination,
    ) {
        OnAppStartNavGraph()
        RegisterUserNavGraph()
    }

}
