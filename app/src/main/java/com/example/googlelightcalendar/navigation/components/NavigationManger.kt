package com.example.googlelightcalendar.navigation.components

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

class NavigationManger @Inject constructor(
    private val externalScope: CoroutineScope,
) {
    val navigationState = MutableSharedFlow<Navigation>()
    val onBackSpace = MutableSharedFlow<Int>()

    fun navigate(
        navigation: Navigation,
        parameters: Map<String, String> = emptyMap(),
        ){
        externalScope.launch {
            navigationState.emit(
                NavigationDestinations.buildDestination(
                    navigation,
                    parameters
                )
            )
        }
    }
}