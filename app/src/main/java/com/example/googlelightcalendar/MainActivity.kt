package com.example.googlelightcalendar

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.navigation
import androidx.navigation.compose.rememberNavController
import com.example.googlelightcalendar.core.main_screen.BottomNavViewModel
import com.example.googlelightcalendar.navigation.components.AuthNavManager
import com.example.googlelightcalendar.navigation.components.MainScreenNavManager
import com.example.googlelightcalendar.navigation.components.MainScreenNavigation
import com.example.googlelightcalendar.navigation.components.MainScreenNavigation.Companion.screens
import com.example.googlelightcalendar.navigation.components.NavigationDestinations
import com.example.googlelightcalendar.navigation.components.ProfileRoutes
import com.example.googlelightcalendar.screens.food_diary.DiaryScreen
import com.example.googlelightcalendar.screens.loginScreen.InitialScreen
import com.example.googlelightcalendar.screens.profile.ProfileScreen
import com.example.googlelightcalendar.screens.register.PhysicalDetailScreen
import com.example.googlelightcalendar.screens.register.RegisterGoalsScreen
import com.example.googlelightcalendar.screens.register.RegistrationScreen
import com.example.googlelightcalendar.ui.theme.GoogleLightCalendarTheme
import com.example.googlelightcalendar.ui.theme.appColor
import com.example.googlelightcalendar.ui.theme.yellowMain
import com.example.googlelightcalendar.ui_components.ScreenUnavailable
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
fun root(
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
                ) {
                    navControl.popBackStack()
                }
            }

            RegisterUserPath()
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun MainScreen(
    userId: String,
    logout: () -> Unit = {},
) {

    val vm = hiltViewModel<BottomNavViewModel>()
    val navController = rememberNavController()


    //updates the bottomNavigationBar when user hits the back space bar.
    vm.setOnBackPressCallBack {
        navController.popBackStack(
            route = MainScreenNavigation.Home.destination,
            inclusive = false,
        )
        vm.updateBottomBarTab(
            navController.currentBackStackEntry?.destination?.route
        )
    }


    //clear stack and logout
    vm.setLogoutCallBack {
        navController.clearBackStack(
            MainScreenNavigation.Home.destination,
        )
        logout()
    }

    LaunchedEffect(
        key1 = vm.navigationManager.navigationState,
    ) {
        vm.navigationManager.navigationState.collectLatest { navDirection ->
            navController.navigate(navDirection.destination) {
                this.launchSingleTop = true
            }
        }
    }

    Scaffold(
        modifier = Modifier.background(
            color = appColor,
        ), topBar = {
            Column(
                modifier = Modifier.background(
                    color = appColor,
                ),
            ) {
                TopAppBar(
                    modifier = Modifier.padding(
                        horizontal = 12.dp,
                    ),
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = appColor,
                    ),
                    navigationIcon = {
                        Image(
                            painter = painterResource(
                                id = R.drawable.logo3,
                            ),
                            contentDescription = ""
                        )
                    },
                    title = { },
                    actions = {
                        Image(
                            modifier = Modifier,
                            painter = painterResource(
                                id = R.drawable.notification_bell,
                            ),
                            contentDescription = ""
                        )
                    }
                )
            }
        }, bottomBar = {
            if (screens.map { it.destination }
                    .contains(navController.currentBackStackEntryAsState().value?.destination?.route)) {
                NavigationBar(
                    containerColor = Color.White
                ) {
                    screens.forEachIndexed { index, item ->
                        NavigationBarItem(
                            selected = vm.selectedOption.value == index,
                            onClick = {
                                vm.selectedOption.value = index
                                navController.navigate(
                                    item.destination.replace("{userID}", userId)
                                ) {
                                    this.launchSingleTop = true
                                }
                            }, icon = {
                                Icon(
                                    painterResource(
                                        id = item.icon,
                                    ), contentDescription = null
                                )
                            }, colors = NavigationBarItemDefaults.colors(
                                selectedIconColor = yellowMain, indicatorColor = Color.White
                            )
                        )
                    }
                }
            }
        }) { innerPadding ->
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
                vm.navigationManager
            )
        }

    }

}

fun NavGraphBuilder.ProfileRoutes() {
    composable(
        route = ProfileRoutes.Account.destination
    ) {
        ScreenUnavailable()
    }

    composable(
        route = ProfileRoutes.Notifications.destination
    ) {
        ScreenUnavailable()
    }

    composable(
        route = ProfileRoutes.Calendar.destination
    ) {
        ScreenUnavailable()
    }

    composable(
        route = ProfileRoutes.TOS.destination
    ) {
        ScreenUnavailable()
    }
}

fun NavGraphBuilder.MainScreenRoutes(
    navigationManager: MainScreenNavManager,
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
                ), text = "You are in the home"
        )
    }

    composable(
        route = MainScreenNavigation.Diary.destination
    ) {
        DiaryScreen()
    }

    composable(
        route = MainScreenNavigation.Calendar.destination
    ) {
        BackHandler {
            navigationManager.onBackPress()
        }
        Text(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    color = appColor,
                ), text = ""
        )
    }

    composable(
        route = ProfileRoutes.Profile.destination
    ) {
        ProfileScreen()
    }

    ProfileRoutes()
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
