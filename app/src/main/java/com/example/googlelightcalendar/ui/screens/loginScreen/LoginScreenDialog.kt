package com.example.googlelightcalendar.ui.screens.loginScreen

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.PreviewParameter
import com.example.googlelightcalendar.core.viewmodels.login.LoginScreenStates
import com.example.googlelightcalendar.screens.loginScreen.preview.LoginScreenPreviewProvider
import com.example.googlelightcalendar.ui_components.dialog.ErrorAlertDialog

@Composable
fun LoginScreenDialog(
    @PreviewParameter(LoginScreenPreviewProvider::class)
    state: LoginScreenStates,
    resetLoginScreen: () -> Unit = {},
    navigateToHomeScreen: (userEmail: String) -> Unit = {},
){
    if (state is LoginScreenStates.LoginError) {
        ErrorAlertDialog(
            title = "Login Failed",
            error = state.message,
            onDismiss = resetLoginScreen
        )

    } else if (
        state is LoginScreenStates.RegistrationRequiredState
    ) {
        ErrorAlertDialog(
            title = "Need to Register User",
            error = "feature isn't implemnented",
            onDismiss = resetLoginScreen
        )
    } else if (
        state is LoginScreenStates.UserSignedInState
    ) {
        navigateToHomeScreen(state.email)
    }

}