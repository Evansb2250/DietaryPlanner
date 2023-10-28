package com.example.googlelightcalendar.core.viewmodels.login

import android.content.Intent
import androidx.activity.result.ActivityResultLauncher
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.googlelightcalendar.interfaces.AppAuthClient
import com.example.googlelightcalendar.repo.UserRepository
import com.example.googlelightcalendar.utils.AsyncResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject


const val TAG = "LoginViewModel"

sealed class LoginScreenStates {
    data class LoginScreenState(
        private val initialUserName: String = "",
        private val initialPassword: String = "",
        val loggedInSuccessfully: Boolean = false,
        val isLoginError: Boolean = false,
        val isLoading: Boolean = false,
        val error: LoginError? = null,
    ) : LoginScreenStates() {
        var userName = mutableStateOf(initialUserName)
        var password = mutableStateOf(initialPassword)

        fun isVerifiedPasswordFormat(): Boolean {
            return verifyPassword() && password.value.isNotEmpty()
        }

        fun containsValidCredentials(): Boolean {
            return isVerifiedPasswordFormat() && verifyUserNameFormat()
        }

        fun verifyPassword(): Boolean {
            val regex = Regex("(.)\\\\1*")
            return password.value.length > 6 || password.value.matches(regex)
        }

        fun verifyUserNameFormat(): Boolean {
            val regex = Regex("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\\$")
            return userName.value.matches(regex)
        }
    }


    data class LoginError(val message: String) : LoginScreenStates()
}


@HiltViewModel
class LoginViewModel @Inject constructor(
    private val userRepository: UserRepository,
) : ViewModel(), AppAuthClient {

    val state: MutableStateFlow<LoginScreenStates.LoginScreenState> =
        MutableStateFlow(LoginScreenStates.LoginScreenState())

    fun signInWithGoogle() {
        userRepository.attemptAuthorization()
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
                    state.value = LoginScreenStates.LoginScreenState(
                        initialPassword = userName,
                        loggedInSuccessfully = false,
                        error = LoginScreenStates.LoginError(
                            message = response.message ?: "Unkown error occurred"
                        )
                    )
                }

                is AsyncResponse.Success -> {
                    state.value = LoginScreenStates.LoginScreenState(
                        initialPassword = userName,
                        loggedInSuccessfully = true
                    )
                }
            }
        }
    }


    fun resetLoginScreenState() {
        state.value = LoginScreenStates.LoginScreenState()
    }

    override fun registerAuthLauncher(launcher: ActivityResultLauncher<Intent>) {
        userRepository.registerAuthLauncher(launcher)
    }

    override fun handleAuthorizationResponse(intent: Intent) {
        userRepository.handleAuthorizationResponse(intent) { signedIn, serverResponse ->
            state.update { it ->
                it.copy(
                    isLoginError = !signedIn,
                    error = if (!signedIn) LoginScreenStates.LoginError(serverResponse) else null
                )
            }
        }
    }

}