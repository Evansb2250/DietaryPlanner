package com.example.googlelightcalendar

import android.app.Activity
import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.collectAsState
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.lifecycleScope
import com.example.googlelightcalendar.repo.CalendarRepo
import com.example.googlelightcalendar.screens.LoginScreen
import com.example.googlelightcalendar.viewmodels.LoginViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


const val REQ_ONE_TAP = 100
const val USER_PREFERENCES_NAME = "USER_PREFERENCES_NAME"

class MainActivity : ComponentActivity() {

    lateinit var loginViewModel: LoginViewModel
    private val Context.dataStore by preferencesDataStore(
        name = USER_PREFERENCES_NAME
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val tokenManager = TokenManager(this)

        loginViewModel = LoginViewModel(this, tokenManager)
        loginViewModel.registerAuthLauncher(
            launcher = registerForActivityResult(
                ActivityResultContracts.StartActivityForResult()
            ) { result ->
                run {
                    if (result.resultCode == Activity.RESULT_OK) {
                        loginViewModel.handleAuthorizationResponse(
                            result.data!!,
                        )
                    }
                }

            }
        )

        setContent {
            LoginScreen(
                state = loginViewModel.state.collectAsState().value,
                signOut = {
                    loginViewModel.signOutWithGoogle()
                },
                getCalender = {
                    loginViewModel.getCalendarInfo()
                },
                initiateLogin = {
                    loginViewModel.signInWithGoogle()
                }
            )
        }
    }
}
