package com.example.googlelightcalendar.screens.loginScreen

import android.content.Intent
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.googlelightcalendar.R
import com.example.googlelightcalendar.core.viewmodels.login.LoginScreenStates
import com.example.googlelightcalendar.core.viewmodels.login.LoginViewModel
import com.example.googlelightcalendar.screens.register.RegistrationScreen
import com.example.googlelightcalendar.ui.theme.GoogleLightCalendarTheme
import com.example.googlelightcalendar.ui_components.dialog.ErrorAlertDialog
import com.example.googlelightcalendar.ui_components.divider.CustomDividerText
import com.example.googlelightcalendar.ui_components.textfields.CustomOutlineTextField

val sidePadding = 16.dp

@RequiresApi(Build.VERSION_CODES.P)
@Composable
fun LoginScreen() {
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

    var tabIndex by remember { mutableStateOf(0) }

    val tabs = listOf("Login", "Sign Up")


    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.Black),

        ) {
        Image(
            painter = painterResource(
                id = R.drawable.splash_calendar
            ),
            contentDescription = "",
            modifier = Modifier.fillMaxWidth()
        )

        TabRow(
            modifier = Modifier.align(
                Alignment.CenterHorizontally
            ),
            containerColor = Color.Black,
            selectedTabIndex = tabIndex
        ) {
            tabs.forEachIndexed { index, title ->
                Tab(
                    text = { Text(title) },
                    selected = tabIndex == index,
                    onClick = { tabIndex = index },
                    selectedContentColor = Color.White
                )
            }


        }

        when (tabIndex) {
            0 -> {
                LoginContent(
                    loginState = loginViewModel.state.collectAsState().value,
                    signInManually = loginViewModel::signInManually,
                    initiateGoogleSignIn = loginViewModel::signInWithGoogle,
                    retryLogin = loginViewModel::resetLoginScreenState,
                    navigateToHomeScreen = loginViewModel::navigateToRegisterScreen,
                    navigateToRegisterScreen = loginViewModel::navigateToRegisterScreen,
                )
            }

            else -> RegistrationScreen()
        }
    }
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
        modifier = Modifier.background(Color.Black)
    ) {

        if (loginState is LoginScreenStates.LoginError) {
            ErrorAlertDialog(
                title = "Login Failed",
                error = loginState.message,
                onDismiss = retryLogin
            )

        } else if (
            loginState is LoginScreenStates.RegistrationRequiredState
        ) {
            ErrorAlertDialog(
                title = "Need to Register User",
                error = "feature isn't implemnented",
                onDismiss = retryLogin
            )
        } else if (
            loginState is LoginScreenStates.UserSignedInState
        ) {

            ErrorAlertDialog(
                title = "Feature In Progress",
                error = "feature isn't implemnented",
                onDismiss = retryLogin
            )
        }

        LoginBottomSheet(
            loginState = if (loginState is LoginScreenStates.LoginScreenState) loginState else LoginScreenStates.LoginScreenState(),
            signInManually = signInManually,
            retryLogin = retryLogin,
            initiateGoogleSignIn = initiateGoogleSignIn,
            navigateToHomeScreen = navigateToHomeScreen,
            navigateToRegisterScreen = navigateToRegisterScreen,
        )
    }
}

@Composable
fun LoginBottomSheet(
    loginState: LoginScreenStates.LoginScreenState,
    signInManually: (userName: String, password: String) -> Unit = { _, _ -> },
    retryLogin: () -> Unit = {},
    initiateGoogleSignIn: () -> Unit = {},
    navigateToHomeScreen: () -> Unit = {},
    navigateToRegisterScreen: () -> Unit = {}
) {
    var containsIncompleteCredentials by remember {
        mutableStateOf(false)
    }
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxSize()
            .padding(
                horizontal = sidePadding
            )
            .background(
                color = Color.Black,
            ),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
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
        Spacer(
            modifier = Modifier.size(20.dp)
        )
        CustomOutlineTextField(
            value = loginState.userName.value,
            onValueChange = { userNameUpdate ->
                loginState.userName.value = userNameUpdate
            },
            label = "Email",
        )

        Spacer(
            modifier = Modifier.size(20.dp)
        )

        CustomOutlineTextField(
            value = loginState.password.value,
            onValueChange = { passwordUpdate ->
                loginState.password.value = passwordUpdate
            },
            label = "Password",
        )
        Spacer(
            modifier = Modifier.size(10.dp)
        )
        Text(
            modifier = Modifier.fillMaxWidth(),
            text = "Forgot Password?",
            textAlign = TextAlign.End,
            color = Color.White,
        )

        Spacer(
            modifier = Modifier.size(40.dp)
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

        Spacer(
            modifier = Modifier.size(20.dp)
        )

        CustomDividerText()


        Text(
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Center,
            text = "Or"
        )

        Spacer(
            modifier = Modifier.size(20.dp)
        )

        OutlinedButton(
            shape = RoundedCornerShape(10.dp),
            modifier = Modifier.fillMaxWidth(),
            onClick = initiateGoogleSignIn,
        ) {
            Text(
                text = "Sign up with Google",
                color = Color.White,
            )
        }

        Spacer(
            modifier = Modifier.size(10.dp)
        )
    }
}
