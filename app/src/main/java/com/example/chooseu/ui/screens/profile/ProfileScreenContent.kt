package com.example.chooseu.ui.screens.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.chooseu.core.profile_screen.states.ProfileScreenStates
import com.example.chooseu.core.toolbar_states.ToolBarState
import com.example.chooseu.navigation.components.destinations.GeneralDestinations
import com.example.chooseu.ui.theme.appColor
import com.example.chooseu.ui.ui_components.buttons.SignOutButton
import com.example.chooseu.ui.ui_components.toolbar.ChooseUToolBar

@Composable
fun ProfileScreenContent(
    state: ProfileScreenStates.ProfilePage,
    onNavigateTo:(GeneralDestinations) -> Unit = {},
    onSignOut: () -> Unit,
) {
//Used to prevent taping more than one option at a time.
    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .background(
                color = Color.Red,
            ),
        topBar = {
            ChooseUToolBar(
                toolBarState = ToolBarState.Home(showTrailingIcon = false),
                navigateBack = { /*TODO*/ },
                navigateToActionDestination = {}
            )
        }
    ) { paddingValues ->
        ProfileOptionsComposable(
            state,
            paddingValues,
            navigate = onNavigateTo
        )

        Box(
            modifier = Modifier
                .padding(
                    vertical = 16.dp
                )
                .fillMaxSize(),
            contentAlignment = Alignment.BottomCenter
        ) {
            SignOutButton(
                onSignOut = onSignOut,
            )
        }
    }
}
