package com.example.googlelightcalendar

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.googlelightcalendar.core.main_screen.BottomNavViewModel
import com.example.googlelightcalendar.navigation.components.AuthNavManager
import com.example.googlelightcalendar.navigation.components.BottomNavBarDestinations
import com.example.googlelightcalendar.navigation.components.NavigationDestinations
import com.example.googlelightcalendar.navigation.routes.MainScreenRoutes
import com.example.googlelightcalendar.navigation.routes.RegisterUserRoutes
import com.example.googlelightcalendar.screens.loginScreen.InitialScreen
import com.example.googlelightcalendar.ui.theme.GoogleLightCalendarTheme
import com.example.googlelightcalendar.ui.theme.appColor
import com.example.googlelightcalendar.ui_components.bottomBar.ChooseUBottomBar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var navAuthManager: AuthNavManager

    @RequiresApi(Build.VERSION_CODES.P)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        installSplashScreen()

        this.actionBar?.hide()
        setContent {
            GoogleLightCalendarTheme {
                root(
                    navAuthManager = navAuthManager,
                )
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.P)
@Composable
private fun root(
    navAuthManager: AuthNavManager,
) {
    val navControl = rememberNavController()

    navAuthManager.onBackPressCallback = {
        navControl.popBackStack()
    }

    LaunchedEffect(
        key1 = navAuthManager.navigationState,
    ) {
        navAuthManager.navigationState.collectLatest { navDirection ->
            navControl.navigate(navDirection.destination) {
                this.launchSingleTop
            }
        }
    }

    Scaffold {
        NavHost(
            modifier = Modifier
                .padding(it)
                .background(
                    // to prevent flicker while transitioning
                    color = appColor
                ), navController = navControl, startDestination = "LoginScreen"
        ) {

            composable(
                route = NavigationDestinations.loginScreen.destination
            ) {
                InitialScreen()
            }

            composable(
                route = NavigationDestinations.MainScreen.destination
            ) {

                val email = it.arguments?.getString("userId") ?: " "

                MainScreen(
                    userId = email,
                )
            }

            RegisterUserRoutes()
        }
    }
}

@Composable
private fun MainScreen(
    userId: String,
) {
    val vm = hiltViewModel<BottomNavViewModel>()
    val navController = rememberNavController()
    //Overrides the onBack handler to force user to logout.
    BackHandler {

    }

    LaunchedEffect(key1 = userId){
        vm.resetNavBarTab()
    }


    LaunchedEffect(
        key1 = vm.navigationManager.navigationState,
    ) {
        vm.navigationManager.navigationState.collectLatest { navDirection ->
            navController.popBackStack()
            navController.navigate(navDirection.destination) {
                this.launchSingleTop = true
            }
        }
    }

    Scaffold(
        modifier = Modifier.background(
            color = appColor,
        ),
        bottomBar = {
                ChooseUBottomBar(
                    tabs = vm.navigationsTabs,
                    tabPosition = vm.selectedOption,
                    onClick = { item ->
                        vm.navigate(
                            item,
                            arguments = mapOf(
                                "userID" to userId
                            )
                        )
                    }
                )
        }) { innerPadding ->
        NavHost(
            modifier = Modifier
                .padding(innerPadding)
                .background(
                    // to prevent flicker while transitioning
                    color = appColor
                ),
            navController = navController,
            startDestination = vm.navigationsTabs.get(0).destination
        ) {
            MainScreenRoutes()
        }
    }
}