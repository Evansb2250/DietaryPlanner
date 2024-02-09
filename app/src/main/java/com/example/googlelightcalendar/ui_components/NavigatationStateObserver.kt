package com.example.googlelightcalendar.ui_components

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import com.example.googlelightcalendar.navigation.components.Navigation
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.collectLatest

/***
 * returns the destination to pass to the navController
 */
@Composable
fun NavigatationStateObserver(
    navigationState: SharedFlow<Navigation>,
    navigateToDestination: (Navigation) -> Unit,
) {
    LaunchedEffect(
        key1 = navigationState,
    ) {
        navigationState.collectLatest { navDirection ->
            navigateToDestination(navDirection)
        }
    }
}

