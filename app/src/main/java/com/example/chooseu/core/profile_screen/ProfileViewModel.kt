package com.example.chooseu.core.profile_screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.chooseu.core.dispatcher_provider.DispatcherProvider
import com.example.chooseu.core.profile_screen.states.ProfileScreenStates
import com.example.chooseu.di.VMAssistFactoryModule
import com.example.chooseu.navigation.components.destinations.GeneralDestinations
import com.example.chooseu.navigation.components.navmanagers.MainFlowNavManager
import com.example.chooseu.navigation.components.navmanagers.AuthNavManager
import com.example.chooseu.repo.UserRepository
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.internal.synchronized
import kotlinx.coroutines.launch

@HiltViewModel(
    assistedFactory = VMAssistFactoryModule.ProfileViewModelFactory::class
)
class ProfileViewModel @AssistedInject constructor(
    @Assisted private val userId: String,
    private val bottomNavManager: MainFlowNavManager,
    private val authNavManager: AuthNavManager,
    private val userRepository: UserRepository,
    private val dispatcherProvider: DispatcherProvider,
) : ViewModel() {
    val state = MutableStateFlow(ProfileScreenStates.ProfilePage).asStateFlow()

    fun logout(){
        viewModelScope.launch(dispatcherProvider.main) {
            authNavManager.navigate(
                GeneralDestinations.AuthentificationFlow
            )
            userRepository.clearPrefsOnSignOut()
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
                    "userId" to userId
                )
            )
        }
    }
}