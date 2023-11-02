package com.example.googlelightcalendar.screens.loginScreen

import android.content.Intent
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.rememberBottomSheetScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.googlelightcalendar.core.viewmodels.login.LoginScreenStates
import com.example.googlelightcalendar.core.viewmodels.login.LoginViewModel
import com.example.googlelightcalendar.ui_components.dialog.ErrorAlertDialog

val sidePadding = 16.dp

@Composable
fun LoginScreen(
    navigateToHomeScreen: () -> Unit = {},
    navigateToRegisterScreen: () -> Unit = {}
) {
    val loginViewModel = hiltViewModel<LoginViewModel>()

    val googleSignInLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult(),
        onResult = { result ->
            val googleSignInIntent = result.data as Intent

            if (googleSignInIntent != null) {
                loginViewModel.handleAuthorizationResponse(googleSignInIntent)
            }
        }
    )

    loginViewModel.registerAuthLauncher(
        googleSignInLauncher
    )

    LoginContent(
        loginState = loginViewModel.state.collectAsState().value,
        signInManually = loginViewModel::signInManually,
        initiateGoogleSignIn = loginViewModel::signInWithGoogle,
        retryLogin = loginViewModel::resetLoginScreenState,
        navigateToHomeScreen = navigateToHomeScreen,
        navigateToRegisterScreen = navigateToRegisterScreen,
    )
}

@Composable
fun LoginContent(
    loginState: LoginScreenStates,
    retryLogin: () -> Unit = {},
    signInManually: (userName: String, password: String) -> Unit = { _, _ -> },
    initiateGoogleSignIn: () -> Unit,
    navigateToHomeScreen: () -> Unit = {},
    navigateToRegisterScreen: () -> Unit = {}
) {
    Box(
        modifier = Modifier.padding(

        )
    ) {


        when (loginState) {
            is LoginScreenStates.LoginError -> {
                ErrorAlertDialog(
                    title = "Login Failed",
                    error = loginState.message,
                    onDismiss = retryLogin
                )

                LoginBottomSheet(
                    loginState = LoginScreenStates.LoginScreenState(),
                    signInManually = signInManually,
                    retryLogin = retryLogin,
                    initiateGoogleSignIn = initiateGoogleSignIn,
                    navigateToHomeScreen = navigateToHomeScreen,
                    navigateToRegisterScreen = navigateToRegisterScreen,
                )
            }
            is LoginScreenStates.LoginScreenState -> {
                LoginBottomSheet(
                    loginState = loginState,
                    signInManually = signInManually,
                    retryLogin = retryLogin,
                    initiateGoogleSignIn = initiateGoogleSignIn,
                    navigateToHomeScreen = navigateToHomeScreen,
                    navigateToRegisterScreen = navigateToRegisterScreen,
                )
            }

            is LoginScreenStates.RegistrationRequiredState -> TODO()
            is LoginScreenStates.UserSignInState -> TODO()
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginBottomSheet(
    loginState: LoginScreenStates.LoginScreenState,
    signInManually: (userName: String, password: String) -> Unit = { _, _ -> },
    retryLogin: () -> Unit = {},
    initiateGoogleSignIn: () -> Unit = {},
    navigateToHomeScreen: () -> Unit = {},
    navigateToRegisterScreen: () -> Unit = {}
) {


    val bottomSheetScaffoldState = rememberBottomSheetScaffoldState()
    var containsIncompleteCredentials by remember {
        mutableStateOf(false)
    }

    ModalBottomSheet(
        modifier = Modifier.wrapContentWidth(),
        sheetState = bottomSheetScaffoldState.bottomSheetState,
        onDismissRequest = { },
        shape = RoundedCornerShape(20.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    horizontal = sidePadding
                ),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            if (containsIncompleteCredentials) {
                ErrorAlertDialog(
                    title = "Invalid Credentials",
                    error = "please fill in the required information",
                    onDismiss = {
                        containsIncompleteCredentials = false
                    }
                )
            }

            if (loginState.isLoginError) {
                ErrorAlertDialog(
                    title = "Login Failed",
                    error = loginState.error?.message ?: "Unable to login",
                    onDismiss = retryLogin
                )

            }

            if (loginState.loggedInSuccessfully) {

            }

            Text(
                modifier = Modifier.fillMaxWidth(),
                text = "Pheonix",
                textAlign = TextAlign.Center,
            )
            Spacer(
                modifier = Modifier.size(30.dp)
            )

            OutlinedTextField(
                value = loginState.userName.value,
                onValueChange = { userNameUpdate ->
                    loginState.userName.value = userNameUpdate
                },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(20.dp)
            )

            Spacer(
                modifier = Modifier.size(30.dp)
            )

            OutlinedTextField(
                value = loginState.password.value,
                onValueChange = { passwordUpdate ->
                    loginState.password.value = passwordUpdate
                },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(20.dp)
            )
            Spacer(
                modifier = Modifier.size(20.dp)
            )

            Button(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(10.dp),
                onClick = {
                    if (loginState.containsValidCredentials()) {
                        signInManually(
                            loginState.userName.value,
                            loginState.password.value,
                        )
                    } else {
                        containsIncompleteCredentials = true
                    }
                },
            ) {
                Text(
                    text = "Log in"
                )
            }

            Button(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(10.dp),
                onClick = { /*TODO*/ }
            ) {
                Text(
                    text = "Create account"
                )
            }

            Spacer(
                modifier = Modifier.size(20.dp)
            )

            Divider(
                modifier = Modifier.wrapContentWidth()
            )

            Text(
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center,
                text = "Or"
            )

            Spacer(
                modifier = Modifier.size(20.dp)
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
            ) {
                OutlinedButton(
                    shape = RoundedCornerShape(10.dp),
                    onClick = { /*TODO*/ },
                ) {
                    Text(text = "FaceBook")
                }
                Spacer(
                    modifier = Modifier.size(10.dp)
                )
                OutlinedButton(
                    shape = RoundedCornerShape(10.dp),
                    onClick = initiateGoogleSignIn,
                ) {
                    Text(text = "Google")
                }
            }
            Spacer(
                modifier = Modifier.size(10.dp)
            )
        }
    }


}
