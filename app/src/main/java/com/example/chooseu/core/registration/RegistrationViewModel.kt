package com.example.chooseu.core.registration

import android.content.Intent
import androidx.activity.result.ActivityResultLauncher
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.chooseu.common.Constants
import com.example.chooseu.navigation.components.destinations.GeneralDestinations
import com.example.chooseu.navigation.components.navmanagers.AuthNavManager
import com.example.chooseu.repo.AuthorizationResponseStates
import com.example.chooseu.repo.UserRepository
import com.example.chooseu.utils.TextFieldUtils
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RegistrationViewModel @Inject constructor(
    val registrationCache: UserRegistrationCache,
    val userRepository: UserRepository,
    val navigationManger: AuthNavManager,
) : ViewModel() {

    private val googleScopes = arrayOf(
        Constants.SCOPE_PROFILE,
        Constants.SCOPE_EMAIL,
        Constants.SCOPE_OPENID,
    )


    private var _state: MutableStateFlow<InitialRegistrationState.PersonalInformationState> =
        MutableStateFlow(
            InitialRegistrationState.PersonalInformationState()
        )

    val state = _state.asStateFlow()

    init {
        _state.value = InitialRegistrationState.PersonalInformationState()
    }


    fun signInWithGoogle() {
        userRepository.attemptAuthorization(googleScopes)
    }

    fun onStoreCredentials(
        state: InitialRegistrationState.PersonalInformationState
    ) {
        //Change back to state.registrationComplete()
        if (                   //state.registrationComplete()
            true
        ) {
            registrationCache.storeKey(RegistrationKeys.FirstName, state.firstName)
            registrationCache.storeKey(RegistrationKeys.LASTNAME, state.lastName)
            registrationCache.storeKey(RegistrationKeys.EMAIL, state.email)
            registrationCache.storeKey(RegistrationKeys.PASSWORD, state.password)
            navigateNextPage()
            reset()
        } else {
            _state.value = InitialRegistrationState.PersonalInformationState(
                failedLoginState = InitialRegistrationState.Failed(
                    true,
                    "Form not completed"
                )
            )
        }
    }

    fun registerLauncher(
        launcher: ActivityResultLauncher<Intent>
    ) {
        userRepository.registerAuthLauncher(
            launcher = launcher
        )
    }

    fun handleAuthorizationResponse(intent: Intent) {
        viewModelScope.launch {
            val serverResponse = userRepository.handleAuthorizationResponse(intent)

            when (serverResponse) {
                is AuthorizationResponseStates.FailedResponsState -> {
                    _state.update {
                        InitialRegistrationState.PersonalInformationState(
                            failedLoginState = InitialRegistrationState.Failed(
                                isError = true,
                                errorMessage = "Failed to login into google",
                            )
                        )
                    }
                }

                is AuthorizationResponseStates.FirstTimeUserState -> {
                    _state.value = InitialRegistrationState.PersonalInformationState(
                        firstName = serverResponse.name,
                        email = serverResponse.email
                    )
                }

                is AuthorizationResponseStates.SuccessResponseState -> {
                    _state.value = InitialRegistrationState.PersonalInformationState(
                        firstName = serverResponse.name,
                        email = serverResponse.email
                    )
                }
            }
        }
    }

    fun updatePersonalInformation(state: InitialRegistrationState.PersonalInformationState) {
        _state.update {
            state
        }
    }

    private fun navigateNextPage() {
        navigationManger.navigate(GeneralDestinations.RegisterDetailsDestination)
    }

    fun reset() {
        // reset needed
        _state.update {
            InitialRegistrationState.PersonalInformationState(
                password = ""
            )
        }
    }
}


sealed class InitialRegistrationState {
    data class PersonalInformationState(
        val firstName: String = "",
        val lastName: String = "",
        val email: String = "",
        val password: String = "",
        val failedLoginState: Failed = Failed()
    ) : InitialRegistrationState() {

        fun containsValidFirstName(): Boolean {
            return TextFieldUtils.isValidName(firstName)
        }

        fun containsValidLastName(): Boolean {
            return TextFieldUtils.isValidName(lastName)
        }

        fun containsValidEmail(): Boolean {
            return TextFieldUtils.isValidEmail(email)
        }

        fun containsValidPassword(): Boolean {
            return TextFieldUtils.isValidPassword(password)
        }

        fun registrationComplete(): Boolean {
            return containsValidEmail() &&
                    containsValidPassword() &&
                    containsValidLastName() &&
                    containsValidFirstName()
        }
    }

    data class Failed(
        val isError: Boolean = false,
        val errorMessage: String? = null,
    )

}

