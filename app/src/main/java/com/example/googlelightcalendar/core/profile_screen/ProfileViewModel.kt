package com.example.googlelightcalendar.core.profile_screen

import androidx.lifecycle.ViewModel
import com.example.googlelightcalendar.core.profile_screen.states.ProfileScreenStates
import com.example.googlelightcalendar.navigation.components.destinations.GeneralDestinations
import com.example.googlelightcalendar.navigation.components.navmanagers.AuthNavManager
import com.example.googlelightcalendar.navigation.components.navmanagers.AppNavManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.internal.synchronized
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val bottomNavManager: AppNavManager,
    private val authNavManager: AuthNavManager,
) : ViewModel() {
    val state = MutableStateFlow(ProfileScreenStates.ProfilePage).asStateFlow()

    fun logout(){
        authNavManager.navigate(
            GeneralDestinations.OnAppStartUpDestination
        )
    }
    @OptIn(InternalCoroutinesApi::class)
    fun navigate(
        destinations: GeneralDestinations
    ) {
        synchronized(destinations){
            bottomNavManager.navigate(
                navigation = destinations,
                arguments = mapOf(
                    "userId" to "23"
                )
            )
        }
    }
}