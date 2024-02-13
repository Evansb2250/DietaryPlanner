package com.example.googlelightcalendar.ui.screens.register.details

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
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

private val verticalSpacing = 10.dp
@Preview(
    showBackground = true,
)
@Composable
fun RegistrationScreenContent(
    modifier: Modifier = Modifier,
    @PreviewParameter(RegistrationScreenPreview::class)
    registrationState: InitialRegistrationState.PersonalInformationState,
    updatePersonalInformation: (InitialRegistrationState.PersonalInformationState) -> Unit = {},
    onNext: (state: InitialRegistrationState.PersonalInformationState) -> Unit = {},
    onReset: () -> Unit = {},
    signUpWithGoogle: () -> Unit = {},
) {
    AppColumnContainer(
        modifier = modifier,
        disableBackPress = false,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top,
    ) {

        if (registrationState.failedLoginState.isError) {
            ErrorAlertDialog(
                title = "Error",
                error = registrationState.failedLoginState.errorMessage ?: "unkown",
                onDismiss = onReset
            )
        }

        CustomOutlineTextField(
            modifier = Modifier.padding(
                bottom = verticalSpacing,
            ),
            leadingIcon = imageHolder(
                leadingIcon = R.drawable.avatar_icon, description = "first name avatar"
            ),
            label = "First name",
            value = registrationState.firstName,
            onValueChange = { nameUpdate ->
                updatePersonalInformation(registrationState.copy(firstName = nameUpdate))
            },
        )

        CustomOutlineTextField(
            modifier = Modifier.padding(
                vertical = verticalSpacing,
            ),
            leadingIcon = imageHolder(
                leadingIcon = R.drawable.avatar_icon,
                description = "last name avatar",
            ),
            label = "Last name",
            value = registrationState.lastName,
            onValueChange = { nameUpdate ->
                updatePersonalInformation(registrationState.copy(lastName = nameUpdate))
            },
        )

        CustomOutlineTextField(
            modifier = Modifier.padding(
                vertical = verticalSpacing,
            ),
            leadingIcon = imageHolder(
                leadingIcon = R.drawable.email_envelope, description = "envelope"
            ),
            label = "Email",
            value = registrationState.email,
            onValueChange = { emailUpdate ->
                updatePersonalInformation(registrationState.copy(email = emailUpdate))
            },
        )

        CustomPasswordTextField(
            modifier = Modifier.padding(
                vertical = verticalSpacing,
            ),
            value = registrationState.password,
            onValueChange = { passwordUpdate ->
                updatePersonalInformation(registrationState.copy(password = passwordUpdate))
            },
        )

        Spacer(modifier = Modifier.size(10.dp))

        StandardButton(
            modifier = Modifier.padding(
                vertical = verticalSpacing,
            ),
            text = "Next",
            onClick = { onNext(registrationState) },
        )

        CustomDividerText()

        GoogleButton(
            modifier = Modifier.padding(
                vertical = verticalSpacing,
            ),
            onClick = signUpWithGoogle
        )
        Spacer(modifier = Modifier.size(20.dp))
    }
}