package com.example.googlelightcalendar.core

import androidx.lifecycle.ViewModel
import com.example.googlelightcalendar.navigation.components.destinations.BottomNavBarDestinations
import com.example.googlelightcalendar.navigation.components.navmanagers.AppNavManager
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class CalendarViewModel @Inject constructor(
    val appNavManager: AppNavManager
) : ViewModel() {
    fun onBackPress() {
        appNavManager.navigate(
            BottomNavBarDestinations.Profile,
        )
    }
}