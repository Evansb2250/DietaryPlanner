package com.example.googlelightcalendar.screens.register

import android.content.Intent
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
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
import com.example.googlelightcalendar.core.registration.InitialRegistrationState
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

    val googleSignInLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult(),
        onResult = { result ->
            val googleSignInIntent = result.data as Intent

            if (googleSignInIntent != null) {
                registrationViewModel.handleAuthorizationResponse(googleSignInIntent)
            }
        }
    )

    registrationViewModel.registerLauncher(
        googleSignInLauncher
    )


    RegistrationScreenContent(
        registrationState = registrationViewModel.state.collectAsState(Dispatchers.Main.immediate).value,
        onNext = registrationViewModel::onStoreCredentials,
        onReset = registrationViewModel::reset,
        signUpWithGoogle = registrationViewModel::signInWithGoogle
    )
}

@Composable
private fun RegistrationScreenContent(
    registrationState: InitialRegistrationState.PersonalInformationState,
    onNext: (state: InitialRegistrationState.PersonalInformationState) -> Unit = {},
    onReset: () -> Unit = {},
    signUpWithGoogle: () -> Unit = {},
) {

    AppColumnContainer(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(15.dp),
    ) {

        if(registrationState.failedSignUp.value.isError){
            ErrorAlertDialog(
                title = "Error",
                error = registrationState.failedSignUp.value.errorMessage ?: "unkown",
                onDismiss = onReset
            )
        }
        InitialRegistrationScreen(
            registrationState,
            onNext = onNext,
            signUpWithGoogle = signUpWithGoogle
        )
    }
}

@Composable
private fun InitialRegistrationScreen(
    state: InitialRegistrationState.PersonalInformationState,
    onNext: (state: InitialRegistrationState.PersonalInformationState) -> Unit = {},
    signUpWithGoogle: () -> Unit = {},
) {
    CustomOutlineTextField(
        leadingIcon = imageHolder(
            leadingIcon = R.drawable.avatar_icon, description = "first name avatar"
        ),
        label = "First name",
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
        label = "Last name",
        value = state.lastName.value,
        onValueChange = {
            state.lastName.value = it
        },
    )


    CustomOutlineTextField(
        leadingIcon = imageHolder(
            leadingIcon = R.drawable.email_envelope, description = "envelope"
        ),
        label = "Email",
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
    )

    CustomDividerText()

    GoogleButton(
        onClick = signUpWithGoogle
    )
    Spacer(modifier = Modifier.size(20.dp))
}
