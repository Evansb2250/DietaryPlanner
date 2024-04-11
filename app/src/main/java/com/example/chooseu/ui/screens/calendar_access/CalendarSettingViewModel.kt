package com.example.chooseu.ui.screens.calendar_access

import androidx.lifecycle.ViewModel
import com.example.chooseu.navigation.components.destinations.BottomNavBarDestinations
import com.example.chooseu.navigation.components.navmanagers.AppNavManager
import com.example.chooseu.utils.ViewModelAssistFactory
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject


@HiltViewModel(
    assistedFactory = ViewModelAssistFactory.CalendarSettingFactory ::class,
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