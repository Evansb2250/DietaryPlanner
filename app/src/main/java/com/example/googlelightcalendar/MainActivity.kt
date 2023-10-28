package com.example.googlelightcalendar

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.googlelightcalendar.screens.loginScreen.LoginScreen
import dagger.hilt.android.AndroidEntryPoint


const val REQ_ONE_TAP = 100
const val USER_PREFERENCES_NAME = "USER_PREFERENCES_NAME"

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            root()
        }
    }
}


@Composable
fun root() {
    val navControl = rememberNavController()
    NavHost(
        navController = navControl,
        startDestination = "LoginScreen"
    ) {
        composable(
            route = "LoginScreen"
        ) {
            LoginScreen()
        }

    }
}