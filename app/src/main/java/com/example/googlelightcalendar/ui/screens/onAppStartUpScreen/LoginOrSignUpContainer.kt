package com.example.googlelightcalendar.ui.screens.onAppStartUpScreen

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.example.googlelightcalendar.ui.screens.loginScreen.LoginScreen
import com.example.googlelightcalendar.ui.screens.register.RegistrationScreen
import com.example.googlelightcalendar.ui.ui_components.header.LoginOrSignUpTabAndHeader


enum class OnAppStartUpScreen(
    type: String,
) {
    LOGIN("Login"),
    REGISTER("Register")
}

@RequiresApi(Build.VERSION_CODES.P)
@Composable
fun LoginOrSignUpContainer(
    displayScreen: String = OnAppStartUpScreen.LOGIN.name,
) {
    var screenToShow by remember {
        mutableStateOf(displayScreen)
    }
    Scaffold(
        topBar = {
            LoginOrSignUpTabAndHeader(
                onShowLoginScreen = { screenToShow = OnAppStartUpScreen.LOGIN.toString() },
                onShowRegistrationScreen = { screenToShow = OnAppStartUpScreen.REGISTER.toString() }
            )
        }
    ) { it ->
        when (screenToShow) {
            OnAppStartUpScreen.LOGIN.toString() -> LoginScreen(
                modifier = Modifier.padding(it),
            )

            OnAppStartUpScreen.REGISTER.toString() -> RegistrationScreen(
                modifier = Modifier.padding(it)
            )
        }
    }
}