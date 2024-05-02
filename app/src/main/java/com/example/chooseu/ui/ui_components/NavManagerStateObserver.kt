package com.example.chooseu.ui.ui_components

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.collectLatest

/***
 * returns the destination to pass to the navController
 */
@Composable
fun <T> NavManagerStateObserver(
    navigationState: SharedFlow<T>,
    onNewStateEvent: (T) -> Unit,
) {
    LaunchedEffect(
        key1 = navigationState,
    ) {
        navigationState.collectLatest { newState ->
            onNewStateEvent(newState)
        }
    }
}

