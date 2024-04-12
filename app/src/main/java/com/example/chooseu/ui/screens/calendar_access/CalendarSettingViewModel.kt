package com.example.chooseu.ui.screens.calendar_access

import androidx.lifecycle.ViewModel
import com.example.chooseu.navigation.components.destinations.BottomNavBarDestinations
import com.example.chooseu.navigation.components.navmanagers.AppNavManager
import com.example.chooseu.di.VMAssistFactoryModule
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel


@HiltViewModel(
    assistedFactory = VMAssistFactoryModule.CalendarSettingFactory ::class,
)
class CalendarSettingViewModel @AssistedInject constructor(
    @Assisted private val userId: String,
    private val navManager: AppNavManager,
) : ViewModel() {


    fun getUserId(): String = userId

    fun onBackPress() {
        navManager.navigate(
            BottomNavBarDestinations.Profile,
            arguments = mapOf(
                "userId" to userId,
            )
        )
    }


}