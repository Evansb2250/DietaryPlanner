package com.example.googlelightcalendar

import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.compose.rememberNavController
import com.example.googlelightcalendar.navigation.components.NavigationDestinations
import com.example.googlelightcalendar.navigation.components.NavigationManger
import com.example.googlelightcalendar.screens.loginScreen.LoginScreen
import com.example.googlelightcalendar.screens.register.PhysicalDetailContent
import com.example.googlelightcalendar.screens.register.RegisterGoalsScreen
import com.example.googlelightcalendar.screens.register.RegistrationScreen
import com.example.googlelightcalendar.ui.theme.GoogleLightCalendarTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var navigationManger: NavigationManger
    @RequiresApi(Build.VERSION_CODES.P)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
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

    LaunchedEffect(
        key1 = navigationManger.navigationState,
    ) {
        navigationManger.navigationState.collectLatest { navDirection ->
            Log.d("NavController Destin", "going to ${navDirection.destination}")
            navControl.navigate(navDirection.destination)
        }
    }

    NavHost(
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
