package com.example.googlelightcalendar.ui.screens.loginScreen.preview

import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import com.example.googlelightcalendar.core.viewmodels.login.LoginScreenStates

class LoginScreenPreviewProvider : PreviewParameterProvider<LoginScreenStates.LoginScreenState> {
    override val values: Sequence<LoginScreenStates.LoginScreenState> = sequenceOf(
        LoginScreenStates.LoginScreenState(
            email = "",
            password = "",
        ),
        LoginScreenStates.LoginScreenState(
            email = "sam",
            password = "",
        )
    )
}