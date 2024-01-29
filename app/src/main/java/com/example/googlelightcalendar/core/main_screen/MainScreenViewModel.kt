package com.example.googlelightcalendar.core.main_screen

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.googlelightcalendar.core.toolBarStates.ToolBarState
import com.example.googlelightcalendar.navigation.components.MainScreenNavManager
import com.example.googlelightcalendar.navigation.components.MainScreenNavigation
import com.example.googlelightcalendar.navigation.components.ProfileRoutes
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainScreenViewModel @Inject constructor(
    val navigationManager: MainScreenNavManager,
) : ViewModel() {

    var _toolBarState by mutableStateOf(ToolBarState.Home())
        private set


    var selectedOption by mutableStateOf(0)
        private set

    init {
        viewModelScope.launch {
            navigationManager.currentDestinations.collect {
                updateBottomBarTab(it.destination)
            }
        }
    }


    fun navigateBack() {
        //returns the old view of the ToolBar
        _toolBarState = ToolBarState.Home()
    }


    fun setOnBackPressCallBack(
        callBack: () -> Unit
    ) {
        navigationManager.onBackPressCallback = callBack
    }

    fun navigate(
        index: Int,
        navigationDestinations: MainScreenNavigation,
        arguments: Map<String, String>,
    ) {
        navigationManager.navigate(
            navigation = navigationDestinations,
            arguments = arguments
        )
    }

    fun updateBottomBarTab(route: String? = null) {
        //Needed to update the navigation Item when user hits the back button
        selectedOption = when (route) {
            MainScreenNavigation.Home.destination -> {
                _toolBarState = ToolBarState.Home(
                    showTrailingIcon = true
                )
                0
            }

            MainScreenNavigation.Diary.destination -> {
                _toolBarState = ToolBarState.Home(
                    showTrailingIcon = true
                )
                1
            }

            MainScreenNavigation.Calendar.destination -> {
                _toolBarState = ToolBarState.Home(
                    showTrailingIcon = true
                )
                2
            }

            ProfileRoutes.Profile.destination -> {
                _toolBarState = ToolBarState.Home(
                    showTrailingIcon = false
                )
                3
            }

            else -> {
                _toolBarState = ToolBarState.Home(
                    showTrailingIcon = true
                )
                0
            }
        }
    }
}