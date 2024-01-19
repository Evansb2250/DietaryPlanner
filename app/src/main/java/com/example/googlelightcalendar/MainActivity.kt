package com.example.googlelightcalendar

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.compose.rememberNavController
import com.example.googlelightcalendar.navigation.components.AuthNavManager
import com.example.googlelightcalendar.navigation.components.MainScreenNavManager
import com.example.googlelightcalendar.navigation.components.MainScreenNavigation
import com.example.googlelightcalendar.navigation.components.NavigationDestinations
import com.example.googlelightcalendar.screens.loginScreen.InitialScreen
import com.example.googlelightcalendar.screens.loginScreen.sidePadding
import com.example.googlelightcalendar.screens.register.PhysicalDetailScreen
import com.example.googlelightcalendar.screens.register.RegisterGoalsScreen
import com.example.googlelightcalendar.screens.register.RegistrationScreen
import com.example.googlelightcalendar.ui.theme.GoogleLightCalendarTheme
import com.example.googlelightcalendar.ui.theme.appColor
import com.example.googlelightcalendar.ui.theme.yellowMain
import com.example.googlelightcalendar.ui_components.buttons.StandardButton
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var navAuthManager: AuthNavManager

    @Inject
    lateinit var mainScreenNavManager: MainScreenNavManager

    @RequiresApi(Build.VERSION_CODES.P)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        installSplashScreen()

        this.actionBar?.hide()
        setContent {
            GoogleLightCalendarTheme {
                root(
                    navAuthManager = navAuthManager,
                    mainScreenNavManager = mainScreenNavManager
                )
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.P)
@Composable
fun root(
    navAuthManager: AuthNavManager,
    mainScreenNavManager: MainScreenNavManager
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
                ),
            navController = navControl,
            startDestination = "LoginScreen"
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
                    navigationManager = mainScreenNavManager
                ) {
                    navControl.popBackStack()
                }
            }

            RegisterUserPath()
        }
    }
}

@Composable
private fun MainScreen(
    navigationManager: MainScreenNavManager,
    userId: String,
    logout: () -> Unit = {},
) {
    val navController = rememberNavController()


    var selectedOption = remember() {
        mutableStateOf(0)
    }

    navigationManager.onBackPressCallback = {
        navController.popBackStack(
            route = MainScreenNavigation.Home.destination,
            inclusive = false,
        )
        //Needed to update the navigation Item when user hits the back button
        selectedOption.value = when (
            navController.currentBackStackEntry?.destination?.route
        ) {
            MainScreenNavigation.Home.destination -> 0
            MainScreenNavigation.Diary.destination -> 1
            MainScreenNavigation.Calender.destination -> 2
            MainScreenNavigation.Profile.destination -> 3
            else -> {
                0
            }
        }
    }

    val screens = listOf(
        MainScreenNavigation.Home,
        MainScreenNavigation.Diary,
        MainScreenNavigation.Calender,
        MainScreenNavigation.Profile,
    )

    LaunchedEffect(
        key1 = navigationManager.navigationState,
    ) {
        navigationManager.navigationState.collectLatest { navDirection ->
            navController.navigate(navDirection.destination) {
                this.launchSingleTop = true
            }
        }
    }

    Scaffold(
        modifier = Modifier
            .background(
                color = appColor,
            ),
        topBar = {
            Image(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        color = appColor
                    ),
                painter = painterResource(id = R.drawable.logo3), contentDescription = ""
            )
        },
        bottomBar = {
            NavigationBar(
                containerColor = Color.White
            ) {
                screens.forEachIndexed { index, item ->
                    NavigationBarItem(
                        selected = selectedOption.value == index,
                        onClick = {
                            selectedOption.value = index
                            navController.navigate(
                                item.destination.replace("{userID}", userId)
                            ) {
                                this.launchSingleTop = true
                            }
                        },
                        icon = {
                            Icon(
                                painterResource(
                                    id = item.icon,
                                ), contentDescription = null
                            )
                        },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = yellowMain,
                            indicatorColor = Color.White
                        )
                    )
                }
            }
        }
    ) { innerPadding ->
        NavHost(
            modifier = Modifier
                .padding(innerPadding)
                .background(
                    // to prevent flicker while transitioning
                    color = appColor
                ),
            navController = navController,
            startDestination = MainScreenNavigation.Home.destination
        ) {
            MainScreenRoutes(
                navigationManager
            ) {
                navController.clearBackStack(
                    MainScreenNavigation.Home.destination,
                )
                logout()
            }
        }

    }

}

fun NavGraphBuilder.MainScreenRoutes(
    navigationManager: MainScreenNavManager,
    Logout: () -> Unit = {}
) {
    composable(
        route = MainScreenNavigation.Home.destination
    ) {

        BackHandler {
            navigationManager.onBackPress()
        }
        Text(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    color = appColor,
                ),
            text = "You are in the home"
        )
    }

    composable(
        route = MainScreenNavigation.Diary.destination
    ) {
        BackHandler {
            navigationManager.onBackPress()
        }
        Text(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    color = appColor,
                ),
            text = "You are in the food"
        )
    }

    composable(
        route = MainScreenNavigation.Calender.destination
    ) {
        BackHandler {
            navigationManager.onBackPress()
        }
        Text(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    color = appColor,
                ),
            text = ""
        )
    }

    composable(
        route = MainScreenNavigation.Profile.destination
    ) {

        BackHandler {
            navigationManager.onBackPress()
        }
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(sidePadding),
            verticalArrangement = Arrangement.Bottom
        ) {
            OutlinedButton(
                modifier = Modifier
                    .background(
                        color = Color.Transparent,
                        shape = RoundedCornerShape(40.dp)
                    )
                    .fillMaxWidth(),
                onClick = Logout,
            ) {

                Text(
                    color = Color.White,
                    text = "Logout"
                )
            }
        }
    }
}

fun NavGraphBuilder.RegisterUserPath() {
    navigation(
        route = NavigationDestinations.RegistrationPath.destination,
        startDestination = NavigationDestinations.RegisterScreen.destination
    ) {
        composable(
            route = NavigationDestinations.RegisterScreen.destination
        ) {
            RegistrationScreen()
        }

        composable(
            route = NavigationDestinations.RegisterPhysicalScreen.destination
        ) {
            PhysicalDetailScreen()
        }
        composable(
            route = NavigationDestinations.RegisterGoalsScreen.destination
        ) {
            RegisterGoalsScreen()
        }
    }
}
