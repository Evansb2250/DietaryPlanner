package com.example.googlelightcalendar.core.main_screen

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.googlelightcalendar.navigation.components.destinations.BottomNavBarDestinations
import com.example.googlelightcalendar.navigation.components.navmanagers.AppNavManager
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class BottomNavViewModel @Inject constructor(
    val navigationManager: AppNavManager,
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
        isVisible = currentScreen in navigationsTabs.map { it.destination }
        selectedOption = navigationsTabs.firstOrNull { it.destination == currentScreen }?.routeId ?: 0

    }
}