package com.example.googlelightcalendar.core.viewmodels.login

import android.content.Intent
import androidx.activity.result.ActivityResultLauncher
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.room.util.copy
import com.example.googlelightcalendar.common.Constants
import com.example.googlelightcalendar.interfaces.AppAuthClient
import com.example.googlelightcalendar.repo.UserRepository
import com.example.googlelightcalendar.utils.AsyncResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

const val TAG = "LoginViewModel"

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val userRepository: UserRepository,
) : ViewModel(), AppAuthClient {

    private val googleScopes = arrayOf(
        Constants.SCOPE_PROFILE,
        Constants.SCOPE_EMAIL,
        Constants.SCOPE_OPENID,
        Constants.CALENDAR_SCOPE,
        Constants.CALENDAR_EVENTS,
        Constants.CALENDAR_READ_ONLY,
    )

    private val _state: MutableStateFlow<LoginScreenStates.LoginScreenState> = MutableStateFlow(
        LoginScreenStates.LoginScreenState(
            isLoading = true,
        )
    )

    val state: StateFlow<LoginScreenStates.LoginScreenState> = _state.asStateFlow()
    fun signInWithGoogle() {
        userRepository.attemptAuthorization(googleScopes)
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
                    _state.value = LoginScreenStates.LoginScreenState(
                        loggedInSuccessfully = false,
                        error = LoginScreenStates.LoginError(
                            message = response.message ?: "Unkown error occurred"
                        )
                    )
                }

                is AsyncResponse.Success -> {
                    _state.value = LoginScreenStates.LoginScreenState(
                        initialPassword = userName,
                        loggedInSuccessfully = true
                    )
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
        userRepository.handleAuthorizationResponse(intent) { signedIn, serverResponse ->
            _state.update { it ->
                it.copy(
                    loggedInSuccessfully = signedIn,
                    isLoading = false,
                    error = if (!signedIn) LoginScreenStates.LoginError(serverResponse) else null
                )
            }
        }
    }
}