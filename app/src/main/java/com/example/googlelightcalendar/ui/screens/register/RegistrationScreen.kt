package com.example.googlelightcalendar.ui.screens.register

import android.content.Intent
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.googlelightcalendar.core.registration.RegistrationViewModel
import com.example.googlelightcalendar.ui.screens.register.details.RegistrationScreenContent
import kotlinx.coroutines.Dispatchers


@Composable
fun RegistrationScreen(
    modifier: Modifier = Modifier,
    registrationViewModel : RegistrationViewModel = hiltViewModel(),
    googleSignInLauncher : ManagedActivityResultLauncher<Intent, ActivityResult> = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult(),
        onResult = { result ->
            val googleSignInIntent = result.data as Intent
            registrationViewModel.handleAuthorizationResponse(googleSignInIntent)
        }
    )
) {

    LaunchedEffect(key1 = Unit){
        registrationViewModel.registerLauncher(
            googleSignInLauncher
        )
    }


    RegistrationScreenContent(
        modifier = modifier,
        registrationState = registrationViewModel.state.collectAsState(Dispatchers.Main.immediate).value,
        onNext = registrationViewModel::onStoreCredentials,
        onReset = registrationViewModel::reset,
        signUpWithGoogle = registrationViewModel::signInWithGoogle,
    )
}