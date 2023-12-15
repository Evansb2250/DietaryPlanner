package com.example.googlelightcalendar.core.registration

import android.content.Intent
import androidx.activity.result.ActivityResultLauncher
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.googlelightcalendar.common.Constants
import com.example.googlelightcalendar.navigation.components.NavigationDestinations.registerPhysicalScreen
import com.example.googlelightcalendar.navigation.components.NavigationManger
import com.example.googlelightcalendar.repo.AuthorizationResponseStates
import com.example.googlelightcalendar.repo.UserRepository
import com.example.googlelightcalendar.utils.TextFieldUtils
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RegistrationViewModel @Inject constructor(
    val registrationCache: UserRegistrationCache,
    val userRepository: UserRepository,
    val navigationManger: NavigationManger,
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
        if (state.registrationComplete()) {
            registrationCache.storeKey(RegistrationKeys.FirstName, state.firstName.value)
            registrationCache.storeKey(RegistrationKeys.LASTNAME, state.lastName.value)
            registrationCache.storeKey(RegistrationKeys.EMAIL, state.email.value)
            registrationCache.storeKey(RegistrationKeys.PASSWORD, state.password.value)

            navigateNextPage()
            reset()
        } else {
            _state.value = InitialRegistrationState.PersonalInformationState(
                initialFailedLoginState = InitialRegistrationState.Failed(
                    true,
                    "Form not completed"
                )
            )
        }
    }

    fun registerLauncher(
        launcher: ActivityResultLauncher<Intent>
    ){
        userRepository.registerAuthLauncher(
            launcher = launcher
        )
    }

    fun handleAuthorizationResponse(intent: Intent) {
        viewModelScope.launch(Dispatchers.IO) {
            userRepository.handleAuthorizationResponse(intent) { serverResponse ->

                when (serverResponse) {
                    is AuthorizationResponseStates.FailedResponsState -> {
                        _state.value = InitialRegistrationState.PersonalInformationState (
                            initialFailedLoginState = InitialRegistrationState.Failed(
                                isError = true,
                                errorMessage = "Failed to login into google",
                            )
                        )
                    }

                    is AuthorizationResponseStates.FirstTimeUserState -> {
                        _state.value = InitialRegistrationState.PersonalInformationState (
                            initialFirstName =  serverResponse.name,
                            initialEmail = serverResponse.email
                        )                    }

                    is AuthorizationResponseStates.SuccessResponseState -> {
                      _state.value = InitialRegistrationState.PersonalInformationState (
                                initialFirstName = serverResponse.name ,
                                initialEmail = serverResponse.email
                            )
                    }
                    else -> {}
                }
            }
        }
    }


    private fun navigateNextPage() {
        navigationManger.navigate(registerPhysicalScreen)
    }

    fun reset() {
        // reset needed
        _state.value = InitialRegistrationState.PersonalInformationState()
    }
}


sealed class InitialRegistrationState : RegistrationScreenStates() {
    data class PersonalInformationState(
        private val initialFirstName: String = "",
        private val initialLastName: String = "",
        private val initialEmail: String = "",
        private val initialPassword: String = "",
        private val initialFailedLoginState: Failed = Failed()
    ) : InitialRegistrationState() {

        var firstName = mutableStateOf(initialFirstName)
        var lastName = mutableStateOf(initialLastName)
        var email = mutableStateOf(initialEmail)
        var password = mutableStateOf(initialPassword)
        var failedSignUp = mutableStateOf(initialFailedLoginState)

        fun containsValidFirstName(): Boolean {
            return TextFieldUtils.isValidName(firstName.value)
        }

        fun containsValidLastName(): Boolean {
            return TextFieldUtils.isValidName(lastName.value)
        }

        fun containsValidEmail(): Boolean {
            return TextFieldUtils.isValidEmail(email.value)
        }

        fun containsValidPassword(): Boolean {
            return TextFieldUtils.isValidPassword(password.value)
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

