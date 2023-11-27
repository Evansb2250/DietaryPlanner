package com.example.googlelightcalendar.screens.register

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Divider
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
import com.example.googlelightcalendar.core.registration.RegistrationScreenStates.RegistrationStatesPageOne.Success
import com.example.googlelightcalendar.core.registration.RegistrationViewModel
import com.example.googlelightcalendar.navigation.components.NavigationDestinations.registerPhysicalScreen
import com.example.googlelightcalendar.screens.loginScreen.sidePadding
import com.example.googlelightcalendar.ui_components.dialog.ErrorAlertDialog
import com.example.googlelightcalendar.ui_components.dialog.ToBeImplementedDialog
import com.example.googlelightcalendar.ui_components.text_fields.CustomOutlineTextField
import com.example.googlelightcalendar.ui_components.text_fields.CustomPasswordTextField


@Composable
fun RegistrationScreen(
    registrationViewModel: RegistrationViewModel = hiltViewModel(),
) {
    BackHandler {
        registrationViewModel.onBackSpace()
    }
    RegistrationScreenContent(
        registrationState = registrationViewModel.state.collectAsState().value,
        onNext = registrationViewModel::onStoreCredentials,
        navigateToNextPage = registrationViewModel::navigateNextPage,
        onReset = registrationViewModel::reset
    )
}

@Composable
private fun RegistrationScreenContent(
    registrationState: RegistrationStatesPageOne,
    onNext: (state: PersonalInformationState) -> Unit = {},
    navigateToNextPage: () -> Unit = {},
    onReset: () -> Unit = {},
) {
    Scaffold { it ->

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

            Success -> {
                navigateToNextPage()
                onReset()
            }
        }
    }
}

@Composable
private fun InitialRegistrationScreen(
    state: PersonalInformationState,
    onNext: (state: PersonalInformationState) -> Unit = {}
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(
                horizontal = sidePadding
            ),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(20.dp),
    ) {
        Spacer(
            modifier = Modifier.size(40.dp)
        )

        Text(
            text = "Welcome to the registration Screen",
        )

        CustomOutlineTextField(
            leadingIcon = imageHolder(
                leadingIcon = R.drawable.avatar_icon, description = "first name avatar"
            ),
            label = "first name",
            text = state.firstName.value,
            onTextChange = {
                state.firstName.value = it
            },
        )

        CustomOutlineTextField(
            leadingIcon = imageHolder(
                leadingIcon = R.drawable.avatar_icon,
                description = "last name avatar",
            ),
            label = "lastName",
            text = state.lastName.value,
            onTextChange = {
                state.lastName.value = it
            },
        )


        CustomOutlineTextField(
            leadingIcon = imageHolder(
                leadingIcon = R.drawable.email_envelope, description = "envelope"
            ),
            label = "email",
            text = state.email.value,
            onTextChange = {
                state.email.value = it
            },
        )


        CustomPasswordTextField(
            text = state.password.value,
            onTextChange = {
                state.password.value = it
            }
        )

        Button(
            onClick = { onNext(state) },
            enabled = !state.registrationComplete()
        ) {
            Text(
                text = "Next"
            )
        }

        Divider()

        OutlinedButton(
            shape = RoundedCornerShape(10.dp),
            onClick = {},
        ) {
            Text(text = "Google")
        }

        Text(
            text = "sign up with google"
        )
    }
}