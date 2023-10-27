package com.example.googlelightcalendar.viewmodels

import android.content.Intent
import androidx.activity.result.ActivityResultLauncher
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.googlelightcalendar.GoogleOauthClient
import com.example.googlelightcalendar.TokenManager
import com.example.googlelightcalendar.repo.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

interface AppAuthClient {
    fun registerAuthLauncher(
        launcher: ActivityResultLauncher<Intent>,
    )

    fun handleAuthorizationResponse(
        intent: Intent,
    )
}

const val TAG = "LoginViewModel"

class LoginScreenValidator() {
    fun verifyUserNameFormat(
        userName: String,
    ): Boolean {
        val regex = Regex("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\\$")
        return userName.matches(regex)
    }

    fun verifyPassword(
        password: String
    ): Boolean {
        val regex = Regex("(.)\\\\1*")
        return password.length > 6 || password.matches(regex)
    }

}

sealed class LoginScreenStates {
    data class LoginScreenState(
        private val initialUserName: String = "",
        private val initialPassword: String = "",
        val isLoginError: Boolean = false,
        val isLoading: Boolean = false,
        val loginScreenValidator: LoginScreenValidator = LoginScreenValidator(),
        val error: Error? = null,
    ) : LoginScreenStates() {
        var userName = mutableStateOf(initialUserName)
        var password = mutableStateOf(initialPassword)


        fun isVerifiedUserNameFormat(): Boolean {
            return loginScreenValidator.verifyUserNameFormat(userName.value)
        }

        fun isVerifiedPasswordFormat(): Boolean {
            return loginScreenValidator.verifyPassword(password.value) && password.value.isNotEmpty()
        }

        fun containsValidCredentials():Boolean{
            return isVerifiedPasswordFormat() && isVerifiedUserNameFormat()
        }
    }

    data class Error(val message: String) : LoginScreenStates()
}


@HiltViewModel
class LoginViewModel @Inject constructor(
   private val userRepository: UserRepository,
) : ViewModel(), AppAuthClient {

    val state = MutableStateFlow(LoginScreenStates.LoginScreenState())

    fun signInWithGoogle() {
        userRepository.attemptAuthorization()
    }

    fun signInManually(
        userName: String,
        password: String,
    ) {
        viewModelScope.launch {
            userRepository.signIn(
                userName,
                password,
            )
        }
    }

    override fun registerAuthLauncher(launcher: ActivityResultLauncher<Intent>) {
        userRepository.registerAuthLauncher(launcher)
    }

    override fun handleAuthorizationResponse(intent: Intent) {
        userRepository.handleAuthorizationResponse(intent){ signedIn, serverResponse ->
            state.update {
                it.copy(
                    isLoginError = !signedIn,
                    error = if (!signedIn) LoginScreenStates.Error(serverResponse) else null
                )
            }
        }
    }

}