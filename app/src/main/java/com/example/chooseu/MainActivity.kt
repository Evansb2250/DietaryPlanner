package com.example.chooseu

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.example.chooseu.ui.theme.GoogleLightCalendarTheme
import dagger.hilt.android.AndroidEntryPoint
import io.appwrite.Client

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @RequiresApi(Build.VERSION_CODES.P)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        installSplashScreen()
        this.actionBar?.hide()
        setContent {
            GoogleLightCalendarTheme {
                App(
                    closeApp = this::finish
                )
            }
        }
    }
}