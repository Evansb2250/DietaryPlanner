package com.example.googlelightcalendar.navigation.components

import com.example.googlelightcalendar.utils.buildDestination
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

/*
Logic is the same between the AuthNav and the MainScreen Nav but each handle different on callback functions and different destinations
made it so that there are 2 types of NavigationManager and the [onBackPressCallback] is retained in their own singleton class.
 */
abstract class NavigationManger(
    private val externalScope: CoroutineScope,
) {
    protected val _navigationState = MutableSharedFlow<Navigation>()
    val navigationState: SharedFlow<Navigation> = _navigationState.asSharedFlow()

    fun navigate(navigation: NavigationDestinations, arguments: Map<String, String> = emptyMap()) {
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

class AuthNavManager @Inject constructor(
    private val externalScope: CoroutineScope,
) : NavigationManger(externalScope) {

    var exitMainScreen: () -> Unit = {}
}

class MainScreenNavManager @Inject constructor(
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

    fun logout() {
        authNavManager.exitMainScreen.invoke()
    }
}