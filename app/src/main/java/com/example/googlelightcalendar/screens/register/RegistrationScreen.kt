package com.example.googlelightcalendar.screens.register

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
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
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.googlelightcalendar.R
import com.example.googlelightcalendar.core.registration.RegistrationScreenStates.RegistrationStatesPageOne
import com.example.googlelightcalendar.core.registration.RegistrationScreenStates.RegistrationStatesPageOne.Failed
import com.example.googlelightcalendar.core.registration.RegistrationScreenStates.RegistrationStatesPageOne.PersonalInformationState
import com.example.googlelightcalendar.core.registration.RegistrationScreenStates.RegistrationStatesPageOne.Success
import com.example.googlelightcalendar.core.registration.RegistrationViewModel
import com.example.googlelightcalendar.screens.loginScreen.sidePadding


@Composable
fun RegistrationScreen(
    registrationViewModel: RegistrationViewModel = hiltViewModel()
) {
    RegistrationScreenContent(
        registrationState = registrationViewModel.state.collectAsState().value
    )
}

@Composable
private fun RegistrationScreenContent(
    registrationState: RegistrationStatesPageOne
) {
    when (registrationState) {
        is Failed -> {}
        is PersonalInformationState -> {
            InitialRegistrationScreen(
                registrationState,
            )
        }

        Success -> TODO()
    }
}

@Composable
private fun InitialRegistrationScreen(
    state: PersonalInformationState,
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
            leadingIcon = IconHolder(
                leadingIcon = R.drawable.avatar_icon, description = "first name avatar"
            ),
            label = "first name",
            text = state.firstName.value,
            onTextChange = {
                state.firstName.value = it
            },
        )

        CustomOutlineTextField(
            leadingIcon = IconHolder(
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
            leadingIcon = IconHolder(
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
            onClick = { /*TODO*/ },
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


@Composable
fun CustomPasswordTextField(
    text: String,
    onTextChange: (String) -> Unit
) {
    var showPassword: Boolean by rememberSaveable {
        mutableStateOf(false)
    }

    OutlinedTextField(
        leadingIcon = {
            Image(
                painter = painterResource(
                    id = R.drawable.password_icon,
                ), contentDescription = "icon of a lock"
            )

        },
        trailingIcon = {

            val trailingIcon = if (!showPassword) {
                R.drawable.hide_password_icon to "hide password"
            } else {
                R.drawable.show_password_icon to "show password"
            }

            Image(
                modifier = Modifier.clickable {
                    showPassword = !showPassword
                },

                painter = painterResource(
                    id = trailingIcon.first,
                ),
                contentDescription = trailingIcon.second,
            )

        },
        value = text,
        onValueChange = onTextChange,
        label = {
            Text(
                text = "password"
            )
        },
        visualTransformation = if (showPassword) VisualTransformation.None else PasswordVisualTransformation()
    )

}

@Composable
fun CustomOutlineTextField(
    leadingIcon: IconHolder? = null,
    label: String? = null,
    text: String,
    onTextChange: (String) -> Unit,
) {

    OutlinedTextField(leadingIcon = {
        if (leadingIcon != null) {
            Image(
                painter = painterResource(
                    id = leadingIcon.leadingIcon,
                ), contentDescription = leadingIcon.description
            )
        }
    },
        value = text,
        onValueChange = onTextChange,
        label = {
        if (label != null) {
            Text(
                text = label
            )
        }
    })
}


data class IconHolder(
    @DrawableRes val leadingIcon: Int,
    val description: String,
)
