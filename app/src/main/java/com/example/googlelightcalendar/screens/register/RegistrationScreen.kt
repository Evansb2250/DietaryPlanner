package com.example.googlelightcalendar.screens.register

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.googlelightcalendar.R
import com.example.googlelightcalendar.common.imageHolder
import com.example.googlelightcalendar.core.registration.RegistrationScreenStates.RegistrationStatesPageOne
import com.example.googlelightcalendar.core.registration.RegistrationScreenStates.RegistrationStatesPageOne.Failed
import com.example.googlelightcalendar.core.registration.RegistrationScreenStates.RegistrationStatesPageOne.PersonalInformationState
import com.example.googlelightcalendar.core.registration.RegistrationViewModel
import com.example.googlelightcalendar.ui_components.buttons.GoogleButton
import com.example.googlelightcalendar.ui_components.buttons.StandardButton
import com.example.googlelightcalendar.ui_components.custom_column.AppColumnContainer
import com.example.googlelightcalendar.ui_components.dialog.ErrorAlertDialog
import com.example.googlelightcalendar.ui_components.divider.CustomDividerText
import com.example.googlelightcalendar.ui_components.text_fields.CustomOutlineTextField
import com.example.googlelightcalendar.ui_components.text_fields.CustomPasswordTextField
import kotlinx.coroutines.Dispatchers


@Composable
fun RegistrationScreen(
    registrationViewModel: RegistrationViewModel = hiltViewModel(),
) {

    RegistrationScreenContent(
        registrationState = registrationViewModel.state.collectAsState(Dispatchers.Main.immediate).value,
        onNext = registrationViewModel::onStoreCredentials,
        onReset = registrationViewModel::reset
    )
}

@Composable
private fun RegistrationScreenContent(
    registrationState: RegistrationStatesPageOne,
    onNext: (state: PersonalInformationState) -> Unit = {},
    onReset: () -> Unit = {},
) {

    AppColumnContainer(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(15.dp),
    ) {
        when (registrationState) {
            is Failed -> {
                ErrorAlertDialog(
                    title = "Error",
                    error = registrationState.errorMessage,
                )
            }

            is PersonalInformationState -> {
                InitialRegistrationScreen(
                    registrationState,
                    onNext = onNext
                )
            }
        }
    }
}

@Composable
private fun InitialRegistrationScreen(
    state: PersonalInformationState,
    onNext: (state: PersonalInformationState) -> Unit = {}
) {
    CustomOutlineTextField(
        leadingIcon = imageHolder(
            leadingIcon = R.drawable.avatar_icon, description = "first name avatar"
        ),
        label = "first name",
        value = state.firstName.value,
        onValueChange = {
            state.firstName.value = it
        },
    )

    CustomOutlineTextField(
        leadingIcon = imageHolder(
            leadingIcon = R.drawable.avatar_icon,
            description = "last name avatar",
        ),
        label = "lastName",
        value = state.lastName.value,
        onValueChange = {
            state.lastName.value = it
        },
    )


    CustomOutlineTextField(
        leadingIcon = imageHolder(
            leadingIcon = R.drawable.email_envelope, description = "envelope"
        ),
        label = "email",
        value = state.email.value,
        onValueChange = {
            state.email.value = it
        },
    )

    CustomPasswordTextField(
        value = state.password.value,
        onValueChange = {
            state.password.value = it
        },
    )

   Spacer(modifier = Modifier.size(10.dp))

    StandardButton(
        text = "Next",
        onClick = { onNext(state) },
        enabled = !state.registrationComplete()
    )

    CustomDividerText()

    GoogleButton(
        onClick = { TODO() }
    )
    Spacer(modifier = Modifier.size(20.dp))
}
