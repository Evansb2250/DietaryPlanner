package com.example.googlelightcalendar.ui.screens.register.details

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import com.example.googlelightcalendar.R
import com.example.googlelightcalendar.common.imageHolder
import com.example.googlelightcalendar.core.registration.InitialRegistrationState
import com.example.googlelightcalendar.screens.register.previews.RegistrationScreenPreview
import com.example.googlelightcalendar.ui.ui_components.buttons.GoogleButton
import com.example.googlelightcalendar.ui.ui_components.buttons.StandardButton
import com.example.googlelightcalendar.ui.ui_components.custom_column.AppColumnContainer
import com.example.googlelightcalendar.ui.ui_components.dialog.ErrorAlertDialog
import com.example.googlelightcalendar.ui.ui_components.divider.CustomDividerText
import com.example.googlelightcalendar.ui.ui_components.text_fields.CustomOutlineTextField
import com.example.googlelightcalendar.ui.ui_components.text_fields.CustomPasswordTextField

@Preview(
    showBackground = true,
)
@Composable
fun RegistrationScreenContent(
    modifier: Modifier = Modifier,
    @PreviewParameter(RegistrationScreenPreview::class)
    registrationState: InitialRegistrationState.PersonalInformationState,
    onNext: (state: InitialRegistrationState.PersonalInformationState) -> Unit = {},
    onReset: () -> Unit = {},
    signUpWithGoogle: () -> Unit = {},
    ) {

    AppColumnContainer(
        modifier = modifier,
        disableBackPress = false,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(15.dp),
    ) {

        if (registrationState.failedSignUp.isError) {
            ErrorAlertDialog(
                title = "Error",
                error = registrationState.failedSignUp.errorMessage ?: "unkown",
                onDismiss = onReset
            )
        }

        CustomOutlineTextField(
            leadingIcon = imageHolder(
                leadingIcon = R.drawable.avatar_icon, description = "first name avatar"
            ),
            label = "First name",
            value = registrationState.firstName,
            onValueChange = registrationState::firstName::set,
        )

        CustomOutlineTextField(
            leadingIcon = imageHolder(
                leadingIcon = R.drawable.avatar_icon,
                description = "last name avatar",
            ),
            label = "Last name",
            value = registrationState.lastName,
            onValueChange = registrationState::lastName::set,
        )

        CustomOutlineTextField(
            leadingIcon = imageHolder(
                leadingIcon = R.drawable.email_envelope, description = "envelope"
            ),
            label = "Email",
            value = registrationState.email,
            onValueChange = registrationState::email::set,
        )

        CustomPasswordTextField(
            value = registrationState.password,
            onValueChange = registrationState::password::set,
        )

        Spacer(modifier = Modifier.size(10.dp))

        StandardButton(
            text = "Next",
            onClick = { onNext(registrationState) },
        )

        CustomDividerText()

        GoogleButton(
            onClick = signUpWithGoogle
        )
        Spacer(modifier = Modifier.size(20.dp))
    }
}