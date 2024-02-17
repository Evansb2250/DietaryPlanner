package com.example.chooseu

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.lifecycleScope
import com.example.chooseu.common.Appwrite
import com.example.chooseu.core.on_startup.OnAppStartUpManager
import com.example.chooseu.ui.theme.GoogleLightCalendarTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @RequiresApi(Build.VERSION_CODES.P)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Appwrite.init(this)
        this.actionBar?.hide()

         val onAppStartUp = OnAppStartUpManager(Appwrite.account)

         var keepScreenOn = true

        lifecycleScope.launch {
            onAppStartUp.fetchSignState()
        }

        installSplashScreen().setKeepOnScreenCondition {
            onAppStartUp.isLoadingData.observe(this) {
                keepScreenOn = it
            }
            keepScreenOn
        }


        setContent {
            GoogleLightCalendarTheme {
                App(
                    lastSignInState = onAppStartUp.lastLoginState,
                    closeApp = this::finish
                )
            }
        }
    }
}