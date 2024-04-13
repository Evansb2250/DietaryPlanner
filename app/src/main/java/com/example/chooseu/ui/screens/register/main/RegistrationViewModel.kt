package com.example.chooseu.ui.screens.register.main

import androidx.lifecycle.ViewModel
import com.example.chooseu.common.Constants.Companion.googleSignNotAvailable
import com.example.chooseu.core.cache.UserRegistrationCache
import com.example.chooseu.core.cache.keys.RegistrationKeys
import com.example.chooseu.ui.screens.register.main.state.InitialRegistrationState
import com.example.chooseu.navigation.components.destinations.GeneralDestinations
import com.example.chooseu.navigation.components.navmanagers.AuthNavManager
import com.example.chooseu.repo.UserRepository
import com.example.chooseu.utils.TexFieldState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class RegistrationViewModel @Inject constructor(
    val registrationCache: UserRegistrationCache,
    val userRepository: UserRepository,
    val navigationManger: AuthNavManager,
) : ViewModel() {

    private var _state: MutableStateFlow<InitialRegistrationState.PersonalInformationState> = MutableStateFlow(
            InitialRegistrationState.PersonalInformationState()
        )

    val state = _state.asStateFlow()

    fun signInWithGoogle() {
        _state.update {
            it.copy(
                failedLoginState = InitialRegistrationState.Failed(
                    isError = true,
                    errorMessage = googleSignNotAvailable
                )
            )
        }
    }

    fun storeCredentialsIntoCache(
        state: InitialRegistrationState.PersonalInformationState
    ) {
        //Change back to state.registrationComplete()
        when (val result = state.registrationComplete()) {
            is TexFieldState.Invalid -> {
                _state.value = InitialRegistrationState.PersonalInformationState(
                    failedLoginState = InitialRegistrationState.Failed(
                        true,
                        result.errorMessage
                    )
                )
            }

            TexFieldState.Passed -> {
                registrationCache.storeKey(RegistrationKeys.FirstName, state.firstName)
                registrationCache.storeKey(RegistrationKeys.LastName, state.lastName)
                registrationCache.storeKey(RegistrationKeys.EMAIL, state.email)
                registrationCache.storeKey(RegistrationKeys.PASSWORD, state.password)
                navigateToFinalRegistrationPage()
                resetRegistrationState()
            }
        }
    }

    fun updatePersonalInformation(state: InitialRegistrationState.PersonalInformationState) {
        _state.update {
            state
        }
    }

    private fun navigateToFinalRegistrationPage() {
        navigationManger.navigate(GeneralDestinations.RegisterDetailsFlow)
    }

    fun resetRegistrationState() {
        // reset needed
        _state.update {
            InitialRegistrationState.PersonalInformationState(
                password = ""
            )
        }
    }
}
