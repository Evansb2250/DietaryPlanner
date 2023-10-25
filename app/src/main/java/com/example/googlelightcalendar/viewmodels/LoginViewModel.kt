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

data class LoginScreenState(
    val state: Boolean = false,
    val googleStringState: String = "",
)

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val googleOauthClient: Lazy<GoogleOauthClient>,
    tokenManager: TokenManager,
) : ViewModel(), AppAuthClient {

    val state = MutableStateFlow<LoginScreenState>(LoginScreenState())

    fun signInWithGoogle() {
        googleOauthClient.value.attemptAuthorization()
    }

    fun signOutWithGoogle() {
        googleOauthClient.value.signOutWithoutRedirect(
            signInState = {
                state.update {
                    it.copy(
                        state = false
                    )
                }
            }
        )
    }

    override fun registerAuthLauncher(launcher: ActivityResultLauncher<Intent>) {
        googleOauthClient.value.registerAuthLauncher(launcher)
    }

    override fun handleAuthorizationResponse(intent: Intent) {
        googleOauthClient.value.handleAuthorizationResponse(
            intent = intent,
            signInState = { newState ->
                state.update {
                    it.copy(
                        state = newState
                    )
                }
                Log.e(TAG, " handle Auth ${state.value}")
            }
        )
    }

}