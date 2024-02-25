package com.example.chooseu.ui.screens.loginScreen

import android.content.Intent
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.chooseu.core.viewmodels.login.LoginScreenStates
import com.example.chooseu.core.viewmodels.login.LoginViewModel
import kotlinx.coroutines.Dispatchers

@Preview(
    showBackground = true,
)
@Composable
fun LoginScreen(
    modifier: Modifier = Modifier,
    loginViewModel: LoginViewModel = hiltViewModel(),
    googleSignInLauncher: ManagedActivityResultLauncher<Intent, ActivityResult> = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult(),
        onResult = { result ->
            val googleSignInIntent = result.data as Intent
            loginViewModel.handleAuthorizationResponse(googleSignInIntent)
        }
    )
) {

//When user exits the screen I want to clear the state.
    DisposableEffect(key1 = Unit) {
        onDispose {
            loginViewModel.resetLoginScreenState()
        }
    }

    LaunchedEffect(key1 = Unit) {
        loginViewModel.registerAuthLauncher(
            googleSignInLauncher
        )
    }


    val state = loginViewModel.state.collectAsState(Dispatchers.Main.immediate).value

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