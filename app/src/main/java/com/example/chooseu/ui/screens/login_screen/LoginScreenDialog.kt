package com.example.chooseu.ui.screens.login_screen

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.PreviewParameter
import com.example.chooseu.ui.screens.login_screen.preview.LoginScreenPreviewProvider
import com.example.chooseu.ui.ui_components.dialog.ErrorDialog
import com.example.chooseu.ui.ui_components.dialog.LoadingDialog

@Composable
fun LoginScreenDialog(
    @PreviewParameter(LoginScreenPreviewProvider::class)
    state: LoginScreenStates,
    resetLoginScreen: () -> Unit = {},
    navigateToHomeScreen: (userId: String) -> Unit = {},
) {
    when (state) {
        LoginScreenStates.Loading -> {
            LoadingDialog()
        }

        is LoginScreenStates.LoginError -> {
            ErrorDialog(
                title = "Login Failed",
                error = state.message,
                onDismiss = resetLoginScreen
            )
        }

        is LoginScreenStates.RegistrationRequiredState -> {
            ErrorDialog(
                title = "Need to Register User",
                error = "feature isn't implemnented",
                onDismiss = resetLoginScreen
            )
        }

        is LoginScreenStates.UserSignedInState -> {
            Log.d("NAVTEST", "Logged In ${state.userId}")
            navigateToHomeScreen(state.userId)
        }

        else -> {}
    }
}