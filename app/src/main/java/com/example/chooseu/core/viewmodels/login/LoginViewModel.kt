package com.example.chooseu.core.viewmodels.login

import android.content.Context
import android.content.Intent
import androidx.activity.result.ActivityResultLauncher
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.chooseu.common.Constants
import com.example.chooseu.navigation.components.destinations.GeneralDestinations
import com.example.chooseu.navigation.components.navmanagers.AuthNavManager
import com.example.chooseu.repo.UserRepository
import com.example.chooseu.utils.AsyncResponse
import com.example.chooseu.utils.TextVerificationStates
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val navigationManager: AuthNavManager,
    private val userRepository: UserRepository,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO,
    private val context: Context,
) : ViewModel() {

    private val googleScopes = arrayOf(
        Constants.SCOPE_PROFILE,
        Constants.SCOPE_EMAIL,
        Constants.SCOPE_OPENID,
//        Constants.CALENDAR_SCOPE,
//        Constants.CALENDAR_EVENTS,
//        Constants.CALENDAR_READ_ONLY,
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

    fun navigateToHomeScreen(
        email: String
    ) {
        navigationManager.navigate(
            navigation = GeneralDestinations.MainScreenDestinations,
            arguments = mapOf(
                "userId" to email
            ),
        )
        resetLoginScreenState()
    }

    fun navigateToRegisterScreen(
        email: String = ""
    ) {

        val parameters = if (email.isEmpty()) {
            emptyMap()
        } else {
            mapOf(
                "email" to email
            )
        }

        navigationManager.navigate(
            navigation = GeneralDestinations.RegistrationDestinations,
            arguments = parameters,
        )
    }

    fun signInManually(
        loginState: LoginScreenStates.LoginScreenState,
    ) {
        when (val result = loginState.containsValidCredentials()) {
            is TextVerificationStates.Invalid -> {
                _state.update {
                    LoginScreenStates.LoginError(result.errorMessage)
                }
            }

            TextVerificationStates.Passed -> {
                signIn(loginState.email, loginState.password)
            }
        }
    }


    private fun signIn(
        email: String,
        password: String,
    ) {
        _state.update {
            LoginScreenStates.Loading
        }
        viewModelScope.launch(dispatcher) {
            try {
                val response = userRepository.signIn(email, password)
                when (response) {
                    is AsyncResponse.Failed -> {
                        _state.update {
                            LoginScreenStates.LoginError(
                                message = response.message ?: "Unkown error occurred"
                            )
                        }
                    }

                    is AsyncResponse.Success -> {
                        if (response.data != null) {
                            _state.update {
                                LoginScreenStates.UserSignedInState(
                                    email = response.data.userName,
                                    name = response.data.name,
                                )
                            }
                        } else {
                            _state.update {
                                LoginScreenStates.LoginError(
                                    message = "Unknown User"
                                )
                            }
                        }
                    }

                    else -> {}
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

    }


    fun resetLoginScreenState() {
        _state.update {
            LoginScreenStates.LoginScreenState()
        }
    }

    fun registerAuthLauncher(launcher: ActivityResultLauncher<Intent>) {
        userRepository.registerAuthLauncher(launcher)
    }

    fun handleAuthorizationResponse(intent: Intent) {
    }

    fun updateLoginState(loginScreenState: LoginScreenStates.LoginScreenState) {
        _state.update { loginScreenState }
    }
}