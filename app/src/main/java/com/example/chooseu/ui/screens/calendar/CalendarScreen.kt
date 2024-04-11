package com.example.chooseu.ui.screens.calendar

import androidx.activity.compose.BackHandler
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import com.example.chooseu.core.toolbar_states.ToolBarState
import com.example.chooseu.ui.ui_components.ScreenUnavailable
import com.example.chooseu.ui.ui_components.toolbar.ChooseUToolBar

@Composable
fun CalendarScreen(
    userId: String,
){
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