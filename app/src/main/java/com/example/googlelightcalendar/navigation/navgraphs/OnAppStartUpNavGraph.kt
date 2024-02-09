package com.example.googlelightcalendar.navigation.navgraphs

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.googlelightcalendar.navigation.components.NavigationDestinations
import com.example.googlelightcalendar.ui.screens.loginScreen.LoginOrSignUpScreen
import com.example.googlelightcalendar.ui.screens.mainScreen.MainScreen

@RequiresApi(Build.VERSION_CODES.P)
fun NavGraphBuilder.OnAppStartRoute() {
    composable(
        route = NavigationDestinations.OnAppStartUp.destination,
        arguments = listOf(
            navArgument("screenType") {
                defaultValue = "Login"
            },
        )
    ) {
        val displayedScreen = it.arguments?.getString("screenType")!!

        LoginOrSignUpScreen(
            displayedScreen,
        )

    }

    composable(
        route = NavigationDestinations.MainScreen.destination,
    ){
        MainScreen(userId = "")

    }


}