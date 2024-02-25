package com.example.chooseu.ui.screens.profile

import androidx.activity.compose.BackHandler
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.chooseu.core.profile_screen.ProfileViewModel
import com.example.chooseu.core.profile_screen.states.ProfileScreenStates

@Preview(
    showBackground = true
)
@Composable
fun ProfileScreen(
) {
    BackHandler {

    }
    val vm: ProfileViewModel = hiltViewModel()
    val state: ProfileScreenStates.ProfilePage = vm.state.collectAsState().value


    ProfileScreenContent(
        state = state,
        onNavigateTo = vm::navigate,
        onSignOut = vm::logout
    )
}
