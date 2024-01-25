package com.example.googlelightcalendar.core.main_screen

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.example.googlelightcalendar.navigation.components.MainScreenNavManager
import com.example.googlelightcalendar.navigation.components.MainScreenNavigation
import com.example.googlelightcalendar.navigation.components.ProfileRoutes
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class BottomNavViewModel @Inject constructor(
    val navigationManager: MainScreenNavManager,
) : ViewModel() {

    var selectedOption = mutableStateOf(0)

    fun setOnBackPressCallBack(
        callBack: () -> Unit
    ) {
        navigationManager.onBackPressCallback = callBack
    }

    fun updateBottomBarTab(route: String? = null){
        //Needed to update the navigation Item when user hits the back button
        selectedOption.value = when (route) {
            MainScreenNavigation.Home.destination -> 0
            MainScreenNavigation.Diary.destination -> 1
            MainScreenNavigation.Calendar.destination -> 2
            ProfileRoutes.Profile.destination -> 3
            else -> {
                0
            }
        }
    }


}