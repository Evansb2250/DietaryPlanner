package com.example.chooseu.navigation.navgraphs.composable

import android.util.Log
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.example.chooseu.navigation.components.destinations.GeneralDestinations
import com.example.chooseu.navigation.components.getUserId
import com.example.chooseu.ui.screens.main_screen.MainScreen

fun NavGraphBuilder.MainScreenFlowComposable(){
    composable(
        route = GeneralDestinations.MainScreenFlow.destination,
    ) {
        MainScreen(
            userId = it.getUserId()
        )
    }
}