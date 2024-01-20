package com.example.googlelightcalendar.core.viewmodels.login

import android.content.Intent
import androidx.activity.result.ActivityResultLauncher
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.googlelightcalendar.common.Constants
import com.example.googlelightcalendar.navigation.components.AuthNavManager
import com.example.googlelightcalendar.navigation.components.NavigationDestinations
import com.example.googlelightcalendar.repo.AuthorizationResponseStates
import com.example.googlelightcalendar.repo.UserRepository
import com.example.googlelightcalendar.utils.AsyncResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val navigationManager: AuthNavManager,
    private val userRepository: UserRepository,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO,
) : ViewModel() {

    private val googleScopes = arrayOf(
        Constants.SCOPE_PROFILE,
        Constants.SCOPE_EMAIL,
        Constants.SCOPE_OPENID,
        Constants.CALENDAR_SCOPE,
        Constants.CALENDAR_EVENTS,
        Constants.CALENDAR_READ_ONLY,
    )

    private val _state: MutableStateFlow<LoginScreenStates> = MutableStateFlow(
        LoginScreenStates.LoginScreenState()
    )

    val state: StateFlow<LoginScreenStates> = _state.asStateFlow()
    fun signInWithGoogle() {
        userRepository.attemptAuthorization(googleScopes)
    }

    private val coroutineExceptionHandler = CoroutineExceptionHandler { _, exception ->
        _state.value = LoginScreenStates.LoginError(
            message = exception.message ?: "Unexpected Error"
        )
    }

    fun navigateToHomeScreen(
        email: String
    ) {
        navigationManager.navigate(
            navigation = NavigationDestinations.MainScreen,
            arguments = mapOf(
                "userId" to email
            ),
        )
        resetLoginScreenState()
    }

    fun navigateToRegisterScreen(
        email: String = ""
    ) {

        val parameters = if (email.isEmpty()) {
            emptyMap()
        } else {
            mapOf(
                "email" to email
            )
        }

        navigationManager.navigate(
            navigation = NavigationDestinations.RegistrationPath,
            arguments = parameters,
        )
    }

    fun signInManually(
        userName: String,
        password: String,
    ) {
        viewModelScope.launch {
            val response = userRepository.signIn(
                userName,
                password,
            )
            when (response) {
                is AsyncResponse.Failed -> {
                    _state.value = LoginScreenStates.LoginError(
                        message = response.message ?: "Unkown error occurred"
                    )
                }

                is AsyncResponse.Success -> {
                    if (response.data != null) {
                        _state.value = LoginScreenStates.UserSignedInState(
                            email = response.data.userName,
                            name = response.data.name,
                        )
                    } else {
                        _state.value = LoginScreenStates.LoginError(
                            message = "Unknown User"
                        )
                    }
                }
            }
        }
    }

    fun resetLoginScreenState() {
        _state.update {
            LoginScreenStates.LoginScreenState()
        }
    }

    fun registerAuthLauncher(launcher: ActivityResultLauncher<Intent>) {
        userRepository.registerAuthLauncher(launcher)
    }

    fun handleAuthorizationResponse(intent: Intent) {
        viewModelScope.launch(dispatcher) {
            userRepository.handleAuthorizationResponse(intent) { serverResponse ->

                when (serverResponse) {
                    is AuthorizationResponseStates.FailedResponsState -> {
                        _state.update {
                            LoginScreenStates.LoginError(
                                message = serverResponse.message,
                            )
                        }
                    }

                    is AuthorizationResponseStates.FirstTimeUserState -> {
                        _state.update {
                            LoginScreenStates.RegistrationRequiredState(
                                email = serverResponse.email,
                            )
                        }
                    }

                    is AuthorizationResponseStates.SuccessResponseState -> {
                        _state.update {
                            LoginScreenStates.UserSignedInState(
                                serverResponse.email,
                                serverResponse.name,
                            )
                        }
                    }
                }
            }
        }
    }
}