package com.example.chooseu

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.runtime.LaunchedEffect
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.datastore.preferences.core.emptyPreferences
import com.example.chooseu.common.DataStoreKeys
import com.example.chooseu.core.on_startup.OnAppStartUpManager
import com.example.chooseu.ui.theme.GoogleLightCalendarTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collectLatest
import java.io.IOException
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var onAppStartUpManager: OnAppStartUpManager

    @RequiresApi(Build.VERSION_CODES.P)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.actionBar?.hide()
        val splashScreen = installSplashScreen()

        setContent {
            LaunchedEffect(key1 = Unit) {
                onAppStartUpManager
                    .getPreferencesFlow()
                    .catch { exception ->
                        if (exception is IOException) {
                            emit(emptyPreferences())
                        } else {
                            throw exception
                        }
                    }.collectLatest { pref ->
                        val experiationDate = pref.get(DataStoreKeys.USER_SESSION_EXPIRATION)
                        val storedUserKey = pref.get(DataStoreKeys.USER_ID)
                        onAppStartUpManager.setOnAppStartUpState(
                            storedUserKey = storedUserKey,
                            experiationDate = experiationDate,
                        )
                    }
            }

            GoogleLightCalendarTheme {
                splashScreen.setKeepOnScreenCondition {
                    !onAppStartUpManager.isfinishedLoading.value
                }

                if (onAppStartUpManager.isfinishedLoading.value) {
                    App(
                        lastSignInState = onAppStartUpManager.lastLoginState,
                        //calls the Finish function to close app
                        closeApp = this::finish
                    )
                }
            }
        }
    }
}