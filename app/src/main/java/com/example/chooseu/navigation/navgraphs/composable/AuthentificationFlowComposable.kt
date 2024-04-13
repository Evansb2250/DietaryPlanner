package com.example.chooseu.navigation.navgraphs.composable

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.chooseu.navigation.components.destinations.GeneralDestinations
import com.example.chooseu.ui.screens.onAppStartUpScreen.LoginOrSignUpContainer

private const val SCREEN_TYPE = "screenType"
private const val LOGIN = "Login"
private fun NavBackStackEntry.getDisplayedScreen(): String = this.arguments?.getString(SCREEN_TYPE) ?: LOGIN

@RequiresApi(Build.VERSION_CODES.P)
fun NavGraphBuilder.AuthentificationFlowComposable() {
    composable(
        route = GeneralDestinations.AuthentificationFlow.destination,
        arguments = listOf(
            navArgument(SCREEN_TYPE) {
                defaultValue = LOGIN
            },
        )
    ) { backstack ->
        LoginOrSignUpContainer(
            displayScreen = backstack.getDisplayedScreen()
        )
    }
}