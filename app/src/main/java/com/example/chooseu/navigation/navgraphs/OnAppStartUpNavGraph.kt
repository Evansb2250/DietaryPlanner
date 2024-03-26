package com.example.chooseu.navigation.navgraphs

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.chooseu.navigation.components.destinations.GeneralDestinations
import com.example.chooseu.ui.screens.main_screen.MainScreen
import com.example.chooseu.ui.screens.onAppStartUpScreen.LoginOrSignUpContainer

/*
 * User can either go to the Login or Registration screen from onAppStartUp. Switching between the two screen is done by a tab and logic and no navigation is used.
 *
 * MainScreen takes the user to the Home Screen where there is a bottom Navigation to go to different screens.
 */
@RequiresApi(Build.VERSION_CODES.P)
fun NavGraphBuilder.OnAppStartNavGraph() {
    composable(
        route = GeneralDestinations.OnAppStartUpDestination.destination,
        arguments = listOf(
            navArgument("screenType") {
                defaultValue = "Login"
            },
        )
    ) {
        val displayedScreen = it.arguments?.getString("screenType")!!
        LoginOrSignUpContainer(
            displayedScreen,
        )
    }

    composable(
        route = GeneralDestinations.MainScreenDestinations.destination,
    ) {
        MainScreen()
    }
}