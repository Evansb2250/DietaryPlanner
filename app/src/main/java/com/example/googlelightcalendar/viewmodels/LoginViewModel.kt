package com.example.googlelightcalendar.viewmodels

import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.activity.result.ActivityResultLauncher
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.googlelightcalendar.GoogleOauthClient
import com.example.googlelightcalendar.TokenManager
import com.example.googlelightcalendar.repo.CalendarRepo
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

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
class LoginViewModel(
    private val context: Context,
    tokenManager: TokenManager,
    private val calendarRepo: CalendarRepo = CalendarRepo(tokenManager)
) : ViewModel(), AppAuthClient {

    val state = MutableStateFlow<LoginScreenState>(LoginScreenState())

    private val googleOauthClient: GoogleOauthClient = GoogleOauthClient(
        context = context,
        tokenManager = tokenManager,
        coroutineScope = viewModelScope
    )

    fun signInWithGoogle() {
        googleOauthClient.attemptAuthorization()
    }

    fun signOutWithGoogle() {
        googleOauthClient.signOutWithoutRedirect(
            signInState = {
                state.update {
                    it.copy(
                        state = false
                    )
                }
            }
        )
    }

    fun getCalendarInfo() {
        if(state.value.state){
            viewModelScope.launch {
                val result = calendarRepo.getCalendar()
                state.update {
                    it.copy(
                        googleStringState = result
                    )
                }
            }
        }
    }

    override fun registerAuthLauncher(launcher: ActivityResultLauncher<Intent>) {
        googleOauthClient.registerAuthLauncher(launcher)
    }

    override fun handleAuthorizationResponse(intent: Intent) {
        googleOauthClient.handleAuthorizationResponse(
            intent = intent,
            signInState = { newState ->
                state.update {
                    it.copy(
                        state = newState
                    )
                }
            //    Log.e(TAG, " handle Auth ${state.value}")
            }
        )
    }

}