package com.example.chooseu.core.app

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.chooseu.core.on_startup.state.LastSignInState
import com.example.chooseu.navigation.components.destinations.GeneralDestinations
import com.example.chooseu.navigation.components.navmanagers.AuthNavManager
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class AppViewModel @Inject constructor(
    val navigationManager: AuthNavManager,
) : ViewModel() {

    var showExitAppNotification by mutableStateOf(false)
        private set

    fun closeExitDialog() {
        showExitAppNotification = false
    }

    fun showExitDialog() {
        showExitAppNotification = true
    }

    fun getStartDestination(onAppStartUpState: LastSignInState): String {
        return when (onAppStartUpState) {
            is LastSignInState.AlreadyLoggedIn -> GeneralDestinations.MainScreenDestinations.destination.replace(
                "userId",
                "${onAppStartUpState.userId}"
            )

            else -> GeneralDestinations.OnAppStartUpDestination.destination
        }
    }
}