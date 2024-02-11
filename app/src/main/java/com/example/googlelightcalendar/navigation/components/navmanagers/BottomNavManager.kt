package com.example.googlelightcalendar.navigation.components.navmanagers

import com.example.googlelightcalendar.navigation.components.destinations.BottomNavBarDestinations
import com.example.googlelightcalendar.utils.buildDestination
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

class BottomNavManager @Inject constructor(
    private val authNavManager: AuthNavManager,
    private val externalScope: CoroutineScope,
) : NavigationManger(externalScope) {

    private val _currentDestinations: MutableStateFlow<BottomNavBarDestinations> =
        MutableStateFlow(BottomNavBarDestinations.Home)

    val currentDestinations = _currentDestinations

    fun navigate(
        navigation: BottomNavBarDestinations,
        arguments: Map<String, String>
    ) {
        externalScope.launch {

            _currentDestinations.emit(navigation)

            _navigationState.emit(
                buildDestination(
                    navigation,
                    arguments,
                )
            )
        }
    }
}