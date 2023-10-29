package com.example.googlelightcalendar.core.viewmodels.login

import android.content.Intent
import androidx.activity.result.ActivityResultLauncher
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.googlelightcalendar.common.Constants
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
        val isLoading: Boolean = false,
        val error: LoginError? = null,
    ) : LoginScreenStates() {
        var userName = mutableStateOf(initialUserName)
        var password = mutableStateOf(initialPassword)
        val isLoginError: Boolean = containsLoginError()

        fun containsValidCredentials(): Boolean {
            return isValidPassword() && isValidEmail()
        }

        fun isValidPassword(): Boolean {
            //checks to see if it contains the same letters
            val passwordRegex = Regex("(.)\\1+")
            return password.value.length > 6 && !passwordRegex.matches(password.value)
        }

        fun isValidEmail(): Boolean {
            val regex = Regex("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}\$")
            return regex.matches(userName.value)
        }

        private fun containsLoginError(): Boolean {
            return error != null
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
        userRepository.attemptAuthorization(
            arrayOf(
                Constants.SCOPE_PROFILE,
                Constants.SCOPE_EMAIL,
                Constants.SCOPE_OPENID,
                Constants.CALENDAR_SCOPE,
                Constants.CALENDAR_EVENTS,
                Constants.CALENDAR_READ_ONLY,
            )
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
                    state.value = LoginScreenStates.LoginScreenState(
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
                    error = if (!signedIn) LoginScreenStates.LoginError(serverResponse) else null
                )
            }
        }
    }

}