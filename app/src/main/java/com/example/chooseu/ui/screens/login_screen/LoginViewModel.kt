package com.example.chooseu.ui.screens.login_screen

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.chooseu.common.Constants.Companion.googleSignNotAvailable
import com.example.chooseu.core.dispatcher_provider.DispatcherProvider
import com.example.chooseu.navigation.components.destinations.GeneralDestinations
import com.example.chooseu.navigation.components.navmanagers.AuthNavManager
import com.example.chooseu.repo.UserRepository
import com.example.chooseu.utils.AsyncResponse
import com.example.chooseu.utils.TexFieldState
import dagger.hilt.android.lifecycle.HiltViewModel
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
    private val dispatcherProvider: DispatcherProvider,
) : ViewModel() {

    private val _state: MutableStateFlow<LoginScreenStates> = MutableStateFlow(
        LoginScreenStates.LoginScreenState()
    )

    val state: StateFlow<LoginScreenStates> = _state.asStateFlow()

    fun attemptSignIn(
        loginState: LoginScreenStates.LoginScreenState,
    ) {
        when (val result = loginState.containsValidCredentials()) {
            is TexFieldState.Invalid -> {
                setErrorState(result.errorMessage)
            }

            TexFieldState.Passed -> {
                signIn(loginState.email, loginState.password)
            }
        }
    }

    private fun signIn(
        email: String,
        password: String,
    ) {
        setStateToLoading()

        viewModelScope.launch(dispatcherProvider.io) {
            try {
                val response = userRepository.signIn(email, password)
                handleSignInResponse(response)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    //Removed prior Oauth code, will be handled using AppWrite.
    fun signInWithGoogle() {
        setErrorState(googleSignNotAvailable)
    }

    fun navigateToHomeScreen(
        userId: String,
    ) {
        Log.d("NAVTEST", "from navigateToHomeScreen $userId")
        navigationManager.navigate(
            navigation = GeneralDestinations.MainScreenFlow,
            arguments = mapOf(
                "userId" to userId,
            )
        )
        resetLoginScreenState()
    }

    private fun setErrorState(errorMessage: String) {
        _state.update {
            LoginScreenStates.LoginError(errorMessage)
        }
    }

    private fun handleSignInResponse(response: AsyncResponse<String>) {

        when (response) {
            is AsyncResponse.Failed -> {
                _state.update {
                    LoginScreenStates.LoginError(
                        message = response.message ?: "Unkown error occurred"
                    )
                }
            }

            is AsyncResponse.Success -> {
                _state.update {
                    LoginScreenStates.UserSignedInState(response.data!!)
                }
            }
        }
    }

    private fun setStateToLoading() {
        _state.update {
            LoginScreenStates.Loading
        }
    }

    fun resetLoginScreenState() {
        _state.update {
            LoginScreenStates.LoginScreenState()
        }
    }

    fun updateLoginState(loginScreenState: LoginScreenStates.LoginScreenState) {
        _state.update { loginScreenState }
    }
}