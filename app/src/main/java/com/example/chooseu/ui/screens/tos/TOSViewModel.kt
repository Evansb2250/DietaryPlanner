package com.example.chooseu.ui.screens.tos

import androidx.lifecycle.ViewModel
import com.example.chooseu.navigation.components.destinations.BottomNavBarDestinations
import com.example.chooseu.navigation.components.navmanagers.MainFlowNavManager
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class TOSViewModel @Inject constructor(
    val appNavManager: MainFlowNavManager
) : ViewModel() {
    fun onBackPress() {
        appNavManager.navigate(
            BottomNavBarDestinations.Profile,
        )
    }

}