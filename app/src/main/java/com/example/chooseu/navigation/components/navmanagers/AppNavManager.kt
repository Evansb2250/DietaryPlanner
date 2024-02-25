package com.example.chooseu.navigation.components.navmanagers

import com.example.chooseu.navigation.components.destinations.BottomNavBarDestinations
import com.example.chooseu.utils.buildDestination
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import javax.inject.Inject

class AppNavManager @Inject constructor(
    private val authNavManager: AuthNavManager,
    private val externalScope: CoroutineScope,
) : NavigationManger(externalScope) {
    fun navigate(
        navigation: BottomNavBarDestinations,
        arguments: Map<String, String>
    ) {
        externalScope.launch {
            _navigationState.emit(
                buildDestination(
                    navigation,
                    arguments,
                )
            )
        }
    }
}