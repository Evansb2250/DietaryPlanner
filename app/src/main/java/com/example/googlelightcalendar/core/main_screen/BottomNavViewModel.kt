package com.example.googlelightcalendar.core.main_screen

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.googlelightcalendar.navigation.components.destinations.BottomNavBarDestinations
import com.example.googlelightcalendar.navigation.components.destinations.GeneralDestinations
import com.example.googlelightcalendar.navigation.components.navmanagers.AuthNavManager
import com.example.googlelightcalendar.navigation.components.navmanagers.BottomNavManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BottomNavViewModel @Inject constructor(
    val authNavManager: AuthNavManager,
    val navigationManager: BottomNavManager,
) : ViewModel() {

    var isVisible by mutableStateOf(true)
        private set

    val navigationsTabs = listOf(
        BottomNavBarDestinations.Home,
        BottomNavBarDestinations.Diary,
        BottomNavBarDestinations.Calendar,
        BottomNavBarDestinations.Profile,
    )

    var selectedOption by mutableStateOf(navigationsTabs[0].routeId)
        private set

    init {
        viewModelScope.launch {
            combine(
                flow = navigationManager.currentDestinations,
                flow2 = navigationManager.navigationState
            ) { bottomNavState: BottomNavBarDestinations, screenState ->
                updateBottomBarTab(bottomNavState, screenState.destination)
            }.collect{

            }
        }
    }

    fun navigate(
        navigationDestinations: BottomNavBarDestinations,
        arguments: Map<String, String>,
    ) {
        navigationManager.navigate(
            navigation = navigationDestinations,
            arguments = arguments
        )
    }

    fun resetNavBarTab() {
        //makes sure we always use the first navTab id in the list to reset it from 0.
        // This we I can remove or comment out screens without having to change my code to point to the
        //write tab.
        selectedOption = navigationsTabs[0].routeId
    }

    fun updateBottomBarTab(route: BottomNavBarDestinations? = null, currentScreen: String? = null) {
        isVisible = currentScreen?.let { currentDestination ->
            navigationsTabs.firstOrNull { it.destination == currentDestination }
        } != null


        selectedOption = route?.routeId ?: 0
    }
}