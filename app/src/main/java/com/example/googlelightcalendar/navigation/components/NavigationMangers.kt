package com.example.googlelightcalendar.navigation.components

import com.example.googlelightcalendar.utils.buildDestination
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableSharedFlow
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
    private val _navigationState = MutableSharedFlow<Navigation>()
    val navigationState: SharedFlow<Navigation> = _navigationState.asSharedFlow()

    abstract var onBackPressCallback: () -> Unit
    fun onBackPress() {
        onBackPressCallback.invoke()
    }
    fun navigate(
        navigation: NavigationDestinations,
        arguments: Map<String, String> = emptyMap(),
    ){
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
    externalScope: CoroutineScope,
) : NavigationManger(externalScope) {
    override var onBackPressCallback: () -> Unit = {}
}

class MainScreenNavManager @Inject constructor(
    externalScope: CoroutineScope,
) : NavigationManger(externalScope) {

    override var onBackPressCallback: () -> Unit = {}
    protected var logoutCallback: () -> Unit = {}

    fun setLogCallBack(
        callBack: () -> Unit
    ){
        logoutCallback = callBack
    }
    fun returnToTopOfStack(){
        logoutCallback()
    }
}