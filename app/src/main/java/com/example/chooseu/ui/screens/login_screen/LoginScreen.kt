package com.example.chooseu.ui.screens.login_screen

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.chooseu.core.viewmodels.login.LoginScreenStates
import com.example.chooseu.core.viewmodels.login.LoginViewModel

@Preview(
    showBackground = true,
)
@Composable
fun LoginScreen(
    modifier: Modifier = Modifier,
    loginViewModel: LoginViewModel = hiltViewModel(),
) {

//When user exits the screen I want to clear the state.
    DisposableEffect(key1 = Unit) {
        onDispose {
            loginViewModel.resetLoginScreenState()
        }
    }

    val state = loginViewModel.state.collectAsStateWithLifecycle().value

    LoginScreenDialog(
        state = state,
        resetLoginScreen = loginViewModel::resetLoginScreenState,
        navigateToHomeScreen = loginViewModel::navigateToHomeScreen,
    )

    LoginScreenContent(
        modifier = modifier,
        loginState = if (state is LoginScreenStates.LoginScreenState) state else LoginScreenStates.LoginScreenState(),
        signInManually = loginViewModel::attemptSignIn,
        initiateGoogleSignIn = loginViewModel::signInWithGoogle,
        updateLoginState = loginViewModel::updateLoginState,
    )
}