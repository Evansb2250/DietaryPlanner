package com.example.googlelightcalendar.core.viewmodels.login

import android.content.Intent
import androidx.activity.result.ActivityResultLauncher
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.googlelightcalendar.common.Constants
import com.example.googlelightcalendar.interfaces.AppAuthClient
import com.example.googlelightcalendar.navigation.components.NavigationDestinations
import com.example.googlelightcalendar.navigation.components.NavigationManger
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
import kotlinx.coroutines.launch
import javax.inject.Inject

const val TAG = "LoginViewModel"

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val navigationManager: NavigationManger,
    private val userRepository: UserRepository,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO,
) : ViewModel(), AppAuthClient {

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

    fun navigateToRegisterScreen(
       email: String? = null
    ){
        navigationManager.navigate(
            NavigationDestinations.registerScreen
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
        _state.value = LoginScreenStates.LoginScreenState()
    }

    override fun registerAuthLauncher(launcher: ActivityResultLauncher<Intent>) {
        userRepository.registerAuthLauncher(launcher)
    }

    override fun handleAuthorizationResponse(intent: Intent) {
        viewModelScope.launch(dispatcher) {
            userRepository.handleAuthorizationResponse(intent) { serverResponse ->

                when (serverResponse) {
                    is AuthorizationResponseStates.FailedResponsState -> {
                        _state.value = LoginScreenStates.LoginError(
                            message = serverResponse.message,
                        )
                    }

                    is AuthorizationResponseStates.FirstTimeUserState -> {
                        _state.value = LoginScreenStates.RegistrationRequiredState(
                            email = serverResponse.email,
                        )
                    }

                    is AuthorizationResponseStates.SuccessResponseState -> {
                        _state.value = LoginScreenStates.UserSignedInState(
                            serverResponse.email,
                            serverResponse.name,
                        )
                    }
                }
            }
        }
    }
}