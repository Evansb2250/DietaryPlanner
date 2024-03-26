package com.example.chooseu.ui.screens.login_screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import com.example.chooseu.R
import com.example.chooseu.common.imageHolder
import com.example.chooseu.core.viewmodels.login.LoginScreenStates
import com.example.chooseu.ui.screens.login_screen.preview.LoginScreenPreviewProvider
import com.example.chooseu.ui.ui_components.buttons.GoogleButton
import com.example.chooseu.ui.ui_components.buttons.StandardButton
import com.example.chooseu.ui.ui_components.custom_column.AppColumnContainer
import com.example.chooseu.ui.ui_components.dialog.ErrorDialog
import com.example.chooseu.ui.ui_components.divider.CustomDividerText
import com.example.chooseu.ui.ui_components.text_fields.CustomOutlineTextField
import com.example.chooseu.ui.ui_components.text_fields.CustomPasswordTextField

@Preview(
    showBackground = true,
)
@Stable
@Composable
fun LoginScreenContent(
    modifier: Modifier = Modifier,
    @PreviewParameter(LoginScreenPreviewProvider::class)
    loginState: LoginScreenStates.LoginScreenState,
    updateLoginState: (LoginScreenStates.LoginScreenState) -> Unit = {},
    signInManually: (LoginScreenStates.LoginScreenState) -> Unit = { },
    initiateGoogleSignIn: () -> Unit = {},
) {

    AppColumnContainer(
        modifier = modifier,
        disableBackPress = false,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        if (loginState.containsIncompleteCredentials) {
            ErrorDialog(
                title = "Invalid Credentials",
                error = "please fill in the required information",
                onDismiss = {
                    updateLoginState(loginState.copy(containsIncompleteCredentials = false))
                }
            )
        }
        CustomOutlineTextField(
            value = loginState.email,
            onValueChange = { userNameUpdate ->
                updateLoginState(loginState.copy(email = userNameUpdate))
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
            value = loginState.password,
            onValueChange = { passwordUpdate ->
                updateLoginState(loginState.copy(password = passwordUpdate))
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
                    signInManually(
                        loginState,
                    )
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
