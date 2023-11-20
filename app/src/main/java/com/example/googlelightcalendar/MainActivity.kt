package com.example.googlelightcalendar

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
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
import com.example.googlelightcalendar.screens.register.RegistrationScreen
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var navigationManger: NavigationManger
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            root(
                navigationManger
            )
        }
    }
}


@Composable
fun root(
    navigationManger: NavigationManger
) {

    val navControl = rememberNavController()

    LaunchedEffect(
        key1 = navigationManger.navigationState,
        ){
        navigationManger.navigationState.collectLatest {navDirection ->
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


fun NavGraphBuilder.RegisterUserPath(){
    navigation(
        route = NavigationDestinations.RegistrationPath.destination,
        startDestination =  NavigationDestinations.registerScreen.destination
    ){
        composable(
            route = NavigationDestinations.registerScreen.destination
        ){
            RegistrationScreen()
        }

        composable(
            route = NavigationDestinations.registerPhysicalScreen.destination
        ){
            @Composable
            fun RegisterUserPhysicalScreen() {

            }

        }
        composable(
            route = NavigationDestinations.registerGoalsScreen.destination
        ){
            @Composable
            fun UserGoalScreen() {

            }

        }

        composable(
            route = NavigationDestinations.registerConfirmationScreen.destination
        ){

        }
    }

}