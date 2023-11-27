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
    private var num = 0

    fun navigate(
        navigation: Navigation,
    ){
        externalScope.launch {
            navigationState.emit(navigation)
        }
    }

    fun onBackSpace(){
        externalScope.launch {
            onBackSpace.emit(num)
            num++
        }
    }
}