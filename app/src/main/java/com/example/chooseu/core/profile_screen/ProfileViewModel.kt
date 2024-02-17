package com.example.chooseu.core.profile_screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.chooseu.core.dispatcher_provider.DispatcherProvider
import com.example.chooseu.core.profile_screen.states.ProfileScreenStates
import com.example.chooseu.navigation.components.destinations.GeneralDestinations
import com.example.chooseu.navigation.components.navmanagers.AuthNavManager
import com.example.chooseu.navigation.components.navmanagers.AppNavManager
import com.example.chooseu.repo.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.internal.synchronized
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val bottomNavManager: AppNavManager,
    private val authNavManager: AuthNavManager,
    private val userRepository: UserRepository,
    private val dispatcherProvider: DispatcherProvider,
) : ViewModel() {
    val state = MutableStateFlow(ProfileScreenStates.ProfilePage).asStateFlow()

    fun logout(){
        viewModelScope.launch(dispatcherProvider.main) {
            userRepository.signOut()
            authNavManager.navigate(
                GeneralDestinations.OnAppStartUpDestination
            )
        }
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