package com.example.googlelightcalendar.viewmodels

import android.content.Intent
import android.util.Log
import androidx.activity.result.ActivityResultLauncher
import androidx.lifecycle.ViewModel
import com.example.googlelightcalendar.GoogleOauthClient
import com.example.googlelightcalendar.TokenManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
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

sealed class LoginScreenStates {
    data class LoginScreenState(
        val userName: String = "",
        val password: String = "",
        val isLoggedIn: Boolean = false,
        val isLoading: Boolean = false,
        val isError: Boolean = false,
        val error: Error? = null,
    ) : LoginScreenStates()

    data class Error(val message: String) : LoginScreenStates()
}


@HiltViewModel
class LoginViewModel @Inject constructor(
    private val googleOauthClient: Lazy<GoogleOauthClient>,
    tokenManager: TokenManager,
) : ViewModel(), AppAuthClient {

    val state = MutableStateFlow(LoginScreenStates.LoginScreenState())

    fun signInWithGoogle() {
        googleOauthClient.value.attemptAuthorization()
    }

    fun signInManually(
        userName: String,
        password: String,
    ){

    }

    override fun registerAuthLauncher(launcher: ActivityResultLauncher<Intent>) {
        googleOauthClient.value.registerAuthLauncher(launcher)
    }

    override fun handleAuthorizationResponse(intent: Intent) {
        googleOauthClient.value.handleAuthorizationResponse(
            intent = intent,
            signInState = { isSignedIn ->
                state.update {
                    it.copy(
                        isLoggedIn = isSignedIn,
                        isError = !isSignedIn,
                        error = if (!isSignedIn) LoginScreenStates.Error(
                            message = "Couldn't Sign In",
                        ) else null
                    )
                }
            }
        )
    }

}