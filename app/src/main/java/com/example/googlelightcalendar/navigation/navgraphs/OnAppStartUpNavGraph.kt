package com.example.googlelightcalendar.navigation.navgraphs

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.googlelightcalendar.navigation.components.NavigationDestinations
import com.example.googlelightcalendar.ui.screens.loginScreen.LoginOrSignUpContainer
import com.example.googlelightcalendar.ui.screens.mainScreen.MainScreen
/*
 * User can either go to the Login or Registration screen from onAppStartUp. Switching between the two screen is done by a tab and logic and no navigation is used.
 *
 * MainScreen takes the user to the Home Screen where there is a bottom Navigation to go to different screens.
 */
@RequiresApi(Build.VERSION_CODES.P)
fun NavGraphBuilder.OnAppStartNavGraph() {

    composable(
        route = NavigationDestinations.OnAppStartUp.destination,
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
        route = NavigationDestinations.MainScreen.destination,
    ){
        MainScreen(userId = "")

    }


}