package com.example.chooseu.ui.screens.register

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.chooseu.core.registration.registration_main.RegistrationViewModel
import com.example.chooseu.ui.screens.register.details.RegistrationScreenContent


@Composable
fun RegistrationScreen(
    modifier: Modifier = Modifier,
    registrationViewModel : RegistrationViewModel = hiltViewModel(),
) {

    //When user exits the screen I want to clear the state.
    DisposableEffect(key1 = Unit){
        onDispose {
            registrationViewModel.resetRegistrationState()        }
    }

    RegistrationScreenContent(
        modifier = modifier,
        registrationState = registrationViewModel.state.collectAsStateWithLifecycle().value,
        onNext = registrationViewModel::storeCredentialsIntoCache,
        onReset = registrationViewModel::resetRegistrationState,
        signUpWithGoogle = registrationViewModel::signInWithGoogle,
        updatePersonalInformation = registrationViewModel::updatePersonalInformation
    )
}