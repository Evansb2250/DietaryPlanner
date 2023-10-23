package com.example.googlelightcalendar.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.example.googlelightcalendar.viewmodels.LoginScreenState
import com.example.googlelightcalendar.viewmodels.LoginViewModel

@Composable
fun LoginScreen(
    state: LoginScreenState,
    initiateLogin: () -> Unit = {},
    getCalender: () -> Unit = {},
    signOut: () -> Unit = {},
) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        if (state.state) {

            Text(
                text = state.googleStringState
            )

            Button(onClick = getCalender ) {
                Text(text = "Get Calendar")
            }
            Button(
                onClick = signOut,
            ) {
                Text(text = "Sign Out")
            }
        } else {
            Button(
                onClick = initiateLogin,
            ) {
                Text("Login")
            }
        }
    }
}