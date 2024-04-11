package com.example.chooseu.ui.screens.notification_screen

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.chooseu.core.NotificationViewModel
import com.example.chooseu.core.toolbar_states.ToolBarState
import com.example.chooseu.ui.ui_components.ScreenUnavailable
import com.example.chooseu.ui.ui_components.custom_column.AppColumnContainer
import com.example.chooseu.ui.ui_components.toolbar.ChooseUToolBar

@Composable
fun NotificationScreen (
    userId: String,
    notificationViewModel: NotificationViewModel = hiltViewModel()
) {
    Scaffold(
        topBar = {
            ChooseUToolBar(
                toolBarState = ToolBarState.Navigated(
                    "Notifications",
                ),
                navigateBack = notificationViewModel::onBackPress,
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