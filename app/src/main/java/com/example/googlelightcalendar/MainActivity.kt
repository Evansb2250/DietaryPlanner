package com.example.googlelightcalendar

import android.app.Activity
import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.compose.runtime.collectAsState
import androidx.datastore.preferences.preferencesDataStore
import com.example.googlelightcalendar.screens.LoginScreen
import com.example.googlelightcalendar.viewmodels.LoginViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject


const val REQ_ONE_TAP = 100
const val USER_PREFERENCES_NAME = "USER_PREFERENCES_NAME"

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @get:Inject
    val loginViewModel: LoginViewModel by viewModels<LoginViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

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
              //      loginViewModel.getCalendarInfo()
                },
                initiateLogin = {
                    loginViewModel.signInWithGoogle()
                }
            )
        }
    }

}
