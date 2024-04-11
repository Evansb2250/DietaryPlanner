package com.example.chooseu.ui.screens.calendar_access

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.chooseu.core.toolbar_states.ToolBarState
import com.example.chooseu.ui.ui_components.ScreenUnavailable
import com.example.chooseu.ui.ui_components.custom_column.AppColumnContainer
import com.example.chooseu.ui.ui_components.toolbar.ChooseUToolBar
import com.example.chooseu.utils.ViewModelAssistFactory

@Composable
fun CalendarSettingScreen(
    userId: String,
    vm: CalendarSettingViewModel = hiltViewModel(
        creationCallback = {factory: ViewModelAssistFactory.CalendarSettingFactory ->
            factory.create(userId)
        }
    )
) {

    Scaffold(
        topBar = {
            ChooseUToolBar(
                toolBarState = ToolBarState.Navigated(
                    "Calendar  ${vm.getUserId()}"
                ),
                navigateBack = vm::onBackPress,
                navigateToActionDestination = {}
            )
        }
    ) { it ->
        AppColumnContainer(
            modifier = Modifier.padding(it),
        ) {

            ScreenUnavailable()
        }
    }
}