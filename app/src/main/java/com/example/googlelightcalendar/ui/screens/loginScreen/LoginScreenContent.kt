package com.example.googlelightcalendar.ui.screens.loginScreen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import com.example.googlelightcalendar.R
import com.example.googlelightcalendar.common.imageHolder
import com.example.googlelightcalendar.core.viewmodels.login.LoginScreenStates
import com.example.googlelightcalendar.screens.loginScreen.preview.LoginScreenPreviewProvider
import com.example.googlelightcalendar.ui_components.buttons.GoogleButton
import com.example.googlelightcalendar.ui_components.buttons.StandardButton
import com.example.googlelightcalendar.ui_components.custom_column.AppColumnContainer
import com.example.googlelightcalendar.ui_components.dialog.ErrorAlertDialog
import com.example.googlelightcalendar.ui_components.divider.CustomDividerText
import com.example.googlelightcalendar.ui_components.text_fields.CustomOutlineTextField
import com.example.googlelightcalendar.ui_components.text_fields.CustomPasswordTextField

@Preview(
    showBackground = true,
)
@Composable
fun LoginScreenContent(
    modifier: Modifier = Modifier,
    @PreviewParameter(LoginScreenPreviewProvider::class)
    loginState: LoginScreenStates.LoginScreenState,
    signInManually: (userName: String, password: String) -> Unit = { _, _ -> },
    initiateGoogleSignIn: () -> Unit = {},
) {
    var containsIncompleteCredentials by remember {
        mutableStateOf(false)
    }

    AppColumnContainer(
        modifier = modifier,
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
