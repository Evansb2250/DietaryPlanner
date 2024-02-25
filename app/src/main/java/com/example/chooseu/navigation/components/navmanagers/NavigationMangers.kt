package com.example.chooseu.navigation.components.navmanagers

import com.example.chooseu.navigation.components.Navigation
import com.example.chooseu.navigation.components.destinations.GeneralDestinations
import com.example.chooseu.utils.buildDestination
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch

/*
Logic is the same between the AuthNav and the MainScreen Nav but each handle different on callback functions and different destinations
made it so that there are 2 types of NavigationManager and the [onBackPressCallback] is retained in their own singleton class.
 */
abstract class NavigationManger(
    private val externalScope: CoroutineScope,
) {
    protected val _navigationState = MutableSharedFlow<Navigation>()
    val navigationState: SharedFlow<Navigation> = _navigationState.asSharedFlow()

    fun navigate(
        navigation: GeneralDestinations,
        arguments: Map<String, String> = emptyMap(),
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