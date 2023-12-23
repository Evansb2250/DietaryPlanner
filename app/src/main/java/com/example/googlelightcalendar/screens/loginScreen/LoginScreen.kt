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
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import coil.compose.rememberAsyncImagePainter
import com.example.googlelightcalendar.R
import com.example.googlelightcalendar.common.imageHolder
import com.example.googlelightcalendar.core.viewmodels.login.LoginScreenStates
import com.example.googlelightcalendar.core.viewmodels.login.LoginViewModel
import com.example.googlelightcalendar.screens.loginScreen.preview.LoginScreenPreviewProvider
import com.example.googlelightcalendar.screens.register.RegistrationScreen
import com.example.googlelightcalendar.ui_components.buttons.GoogleButton
import com.example.googlelightcalendar.ui_components.buttons.StandardButton
import com.example.googlelightcalendar.ui_components.custom_column.AppColumnContainer
import com.example.googlelightcalendar.ui_components.dialog.ErrorAlertDialog
import com.example.googlelightcalendar.ui_components.divider.CustomDividerText
import com.example.googlelightcalendar.ui_components.text_fields.CustomOutlineTextField
import com.example.googlelightcalendar.ui_components.text_fields.CustomPasswordTextField
import kotlinx.coroutines.Dispatchers

val sidePadding = 16.dp

@Preview(
    showBackground = true,
)
@RequiresApi(Build.VERSION_CODES.P)
@Composable
fun InitialScreen() {
    val tabIndex by remember {
        derivedStateOf { mutableStateOf(0) }
    }

    val tabs = listOf("Login", "Sign Up")

    val painter = rememberAsyncImagePainter(R.drawable.chooseuloginlogo)

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.Black),
    ) {
        Image(
            painter = painter,
            contentDescription = "",
            modifier = Modifier.fillMaxWidth()
        )

        TabRow(
            modifier = Modifier.align(
                Alignment.CenterHorizontally
            ),
            containerColor = Color.Black,
            selectedTabIndex = tabIndex.value,
        ) {
            tabs.forEachIndexed { index, title ->
                Tab(
                    text = { Text(title) },
                    selected = tabIndex.value == index,
                    onClick = { tabIndex.value = index },
                    selectedContentColor = Color.White
                )
            }
        }

        when (tabIndex.value) {
            0 -> {
                LoginScreen()
            }

            else -> {
                RegistrationScreen()
            }
        }
    }
}

@Preview(
    showBackground = true,
)
@Composable
private fun LoginScreen() {
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
        loginState = loginViewModel.state.collectAsState(Dispatchers.Main.immediate).value,
        signInManually = loginViewModel::signInManually,
        initiateGoogleSignIn = loginViewModel::signInWithGoogle,
        retryLogin = loginViewModel::resetLoginScreenState,
        navigateToHomeScreen = loginViewModel::navigateToRegisterScreen,
        navigateToRegisterScreen = loginViewModel::navigateToRegisterScreen,
    )
}

@Preview(
    showBackground = true,
)
@Composable
fun LoginContent(
    @PreviewParameter(LoginScreenPreviewProvider::class)
    loginState: LoginScreenStates,
    retryLogin: () -> Unit = {},
    signInManually: (userName: String, password: String) -> Unit = { _, _ -> },
    initiateGoogleSignIn: () -> Unit = {},
    navigateToHomeScreen: () -> Unit = {},
    navigateToRegisterScreen: () -> Unit = {},
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

        LoginContent(
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
private fun LoginContent(
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
    AppColumnContainer(
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
        CustomOutlineTextField(
            value = loginState.email.value,
            onValueChange = { userNameUpdate ->
                loginState.email.value = userNameUpdate
            },
            leadingIcon = imageHolder(
                leadingIcon = R.drawable.email_envelope,
                description = "last name avatar",
            ),
            label = "Email",
        )

        Spacer(
            modifier = Modifier.size(20.dp)
        )


        CustomPasswordTextField(
            value = loginState.password.value,
            onValueChange = { passwordUpdate ->
                loginState.password.value = passwordUpdate
            },
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

        StandardButton(
            text = "Log in",
            onClick = {
                if (loginState.containsValidCredentials()) {
                    signInManually(
                        loginState.email.value,
                        loginState.password.value,
                    )
                } else {
                    containsIncompleteCredentials = true
                }
            },
        )

        Spacer(
            modifier = Modifier.size(20.dp)
        )

        CustomDividerText()

        Spacer(
            modifier = Modifier.size(20.dp)
        )

        GoogleButton(
            onClick = initiateGoogleSignIn,
        )

        Spacer(
            modifier = Modifier.size(10.dp)
        )
    }
}
