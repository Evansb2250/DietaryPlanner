package com.example.googlelightcalendar

import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.navigation
import androidx.navigation.compose.rememberNavController
import com.example.googlelightcalendar.navigation.components.NavigationDestinations
import com.example.googlelightcalendar.navigation.components.NavigationManger
import com.example.googlelightcalendar.screens.loginScreen.LoginContent
import com.example.googlelightcalendar.screens.loginScreen.LoginScreen
import com.example.googlelightcalendar.screens.register.PhysicalDetailContent
import com.example.googlelightcalendar.screens.register.RegisterGoalsScreen
import com.example.googlelightcalendar.screens.register.RegistrationScreen
import com.example.googlelightcalendar.ui.theme.GoogleLightCalendarTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var navigationManger: NavigationManger

    @RequiresApi(Build.VERSION_CODES.P)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen()

        this.actionBar?.hide()
        setContent {
            GoogleLightCalendarTheme {
                root(
                    navigationManger
                )
            }
        }
    }
}


@RequiresApi(Build.VERSION_CODES.P)
@Composable
fun root(
    navigationManger: NavigationManger
) {

    val navControl = rememberNavController()
    val destination = navControl.currentBackStackEntryAsState().value
    LaunchedEffect(
        key1 = navigationManger.navigationState,
    ) {
        navigationManger.navigationState.collectLatest { navDirection ->
            navControl.navigate(navDirection.destination) {
                this.launchSingleTop
            }

            Log.d("NavController Destin", "going to ${navDirection.destination}")
        }
    }

    Scaffold(
        topBar = {


        }
    ) {
        NavHost(
            modifier = Modifier.padding(it),
            navController = navControl,
            startDestination = "LoginScreen"
        ) {
            composable(
                route = NavigationDestinations.loginScreen.destination
            ) {
                LoginScreen()
            }
            RegisterUserPath()
        }
    }


}

fun NavGraphBuilder.RegisterUserPath() {
    navigation(
        route = NavigationDestinations.RegistrationPath.destination,
        startDestination = NavigationDestinations.registerScreen.destination
    ) {
        composable(
            route = NavigationDestinations.registerScreen.destination
        ) {
            RegistrationScreen()
        }

        composable(
            route = NavigationDestinations.registerPhysicalScreen.destination
        ) {
            PhysicalDetailContent()
        }
        composable(
            route = NavigationDestinations.registerGoalsScreen.destination
        ) {
            RegisterGoalsScreen()
        }

        composable(
            route = NavigationDestinations.registerConfirmationScreen.destination
        ) {

        }
    }
}
