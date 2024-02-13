package com.example.googlelightcalendar.ui.screens.mainScreen

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.googlelightcalendar.core.main_screen.BottomNavViewModel
import com.example.googlelightcalendar.navigation.components.destinations.BottomNavBarDestinations
import com.example.googlelightcalendar.navigation.navgraphs.MainScreenRoutes
import com.example.googlelightcalendar.ui.theme.appColor
import com.example.googlelightcalendar.ui.ui_components.bottomBar.ChooseUBottomBar
import kotlinx.coroutines.flow.collectLatest

@Composable
fun MainScreen(
    userId: String,
    vm: BottomNavViewModel = hiltViewModel(),
    navController: NavHostController = rememberNavController()
) {

    BackHandler {

    }

    //needed to check the current destination on BackPress because it isn't tracked through the flow.
    val destinations = navController.currentBackStackEntryAsState().value?.destination

    LaunchedEffect(key1 = destinations ){
        vm.updateBottomBarTab(route = null, currentScreen = destinations?.route)
    }

    LaunchedEffect(key1 = userId) {
        vm.resetNavBarTab()
    }

    LaunchedEffect(
        key1 = vm.navigationManager.navigationState,
    ) {
        vm.navigationManager.navigationState.collectLatest { navDirection ->
            if (navDirection is BottomNavBarDestinations)
                navController.popBackStack()

            navController.navigate(navDirection.destination) {
                this.launchSingleTop = true
                popUpTo(BottomNavBarDestinations.Home.destination) {
                    inclusive = true
                }
            }
        }
    }

    Scaffold(
        modifier = Modifier.background(
            color = appColor,
        ),
        bottomBar = {
            if (vm.isVisible){
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
            startDestination = vm.navigationsTabs.get(0).destination
        ) {
            MainScreenRoutes()
        }
    }
}