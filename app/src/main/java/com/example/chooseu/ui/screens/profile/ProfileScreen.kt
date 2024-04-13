package com.example.chooseu.ui.screens.profile

import androidx.activity.compose.BackHandler
import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.chooseu.ui.screens.profile.states.ProfileScreenStates
import com.example.chooseu.di.VMAssistFactoryModule

@Composable
fun ProfileScreen(
    userId: String,
    vm: ProfileViewModel = hiltViewModel(
        creationCallback = { factory: VMAssistFactoryModule.ProfileViewModelFactory ->
            factory.create(userId)
        }
    )
) {
    BackHandler {

    }

    val state: ProfileScreenStates.ProfilePage = vm.state.collectAsStateWithLifecycle().value

    ProfileScreenContent(
        state = state,
        onNavigateTo = vm::navigate,
        onSignOut = vm::logout
    )
}
