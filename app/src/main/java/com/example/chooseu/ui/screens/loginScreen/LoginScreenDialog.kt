package com.example.chooseu.ui.screens.loginScreen

import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.window.Dialog
import com.example.chooseu.core.viewmodels.login.LoginScreenStates
import com.example.chooseu.ui.screens.loginScreen.preview.LoginScreenPreviewProvider
import com.example.chooseu.ui.ui_components.dialog.ErrorAlertDialog

@Composable
fun LoginScreenDialog(
    @PreviewParameter(LoginScreenPreviewProvider::class)
    state: LoginScreenStates,
    resetLoginScreen: () -> Unit = {},
    navigateToHomeScreen: (userEmail: String) -> Unit = {},
) {
    when (state) {
        LoginScreenStates.Loading -> {
            Dialog(
                onDismissRequest = { /*TODO*/ },
                content = {
                    CircularProgressIndicator()
                }
            )
        }

        is LoginScreenStates.LoginError -> {
            ErrorAlertDialog(
                title = "Login Failed",
                error = state.message,
                onDismiss = resetLoginScreen
            )
        }

        is LoginScreenStates.RegistrationRequiredState -> {
            ErrorAlertDialog(
                title = "Need to Register User",
                error = "feature isn't implemnented",
                onDismiss = resetLoginScreen
            )
        }

        is LoginScreenStates.UserSignedInState -> {
            navigateToHomeScreen(state.email)
        }

        else -> {}
    }
}