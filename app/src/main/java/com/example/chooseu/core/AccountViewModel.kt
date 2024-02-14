package com.example.chooseu.core

import androidx.lifecycle.ViewModel
import com.example.chooseu.navigation.components.destinations.BottomNavBarDestinations
import com.example.chooseu.navigation.components.navmanagers.AppNavManager
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class AccountViewModel @Inject constructor(
    val appNavManager: AppNavManager
) : ViewModel() {
    fun onBackPress() {
        appNavManager.navigate(
            BottomNavBarDestinations.Profile,
        )
    }
}