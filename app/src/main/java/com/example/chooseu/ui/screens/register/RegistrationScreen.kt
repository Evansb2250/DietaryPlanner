package com.example.chooseu.ui.screens.register

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.chooseu.core.registration.registration_main.RegistrationViewModel
import com.example.chooseu.ui.screens.register.details.RegistrationScreenContent
import kotlinx.coroutines.Dispatchers


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
        registrationState = registrationViewModel.state.collectAsState(Dispatchers.Main.immediate).value,
        onNext = registrationViewModel::storeCredentialsIntoCache,
        onReset = registrationViewModel::resetRegistrationState,
        signUpWithGoogle = registrationViewModel::signInWithGoogle,
        updatePersonalInformation = registrationViewModel::updatePersonalInformation
    )
}