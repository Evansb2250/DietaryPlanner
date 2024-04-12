package com.example.chooseu.ui.screens.home

import androidx.activity.compose.BackHandler
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.chooseu.core.toolbar_states.ToolBarState
import com.example.chooseu.ui.ui_components.ScreenUnavailable
import com.example.chooseu.ui.ui_components.toolbar.ChooseUToolBar
import com.example.chooseu.di.VMAssistFactoryModule

@Composable
fun HomeScreen(
    userId: String,
    vm: HomeViewModel = hiltViewModel(
        creationCallback = { factory: VMAssistFactoryModule.HomeViewModelFactory ->
            factory.create(userId)
        }
    )
) {
    Scaffold(
        topBar = {
            ChooseUToolBar(
                toolBarState = ToolBarState.Home(),
                navigateBack = { /*TODO*/ },
                navigateToActionDestination = {}
            )
        }
    ) { it ->
        BackHandler {

        }

        ScreenUnavailable()
    }
}