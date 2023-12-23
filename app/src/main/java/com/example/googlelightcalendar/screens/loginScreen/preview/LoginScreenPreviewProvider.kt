package com.example.googlelightcalendar.screens.loginScreen.preview

import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import com.example.googlelightcalendar.core.viewmodels.login.LoginScreenStates

class LoginScreenPreviewProvider : PreviewParameterProvider<LoginScreenStates.LoginScreenState> {
    override val values: Sequence<LoginScreenStates.LoginScreenState> = sequenceOf(
        LoginScreenStates.LoginScreenState(
            initialUserName = "",
            initialPassword = "",
        ),
        LoginScreenStates.LoginScreenState(
            initialUserName = "sam",
            initialPassword = "",
        )
    )
}