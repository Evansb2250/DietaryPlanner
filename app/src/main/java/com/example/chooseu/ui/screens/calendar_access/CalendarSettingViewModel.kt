package com.example.chooseu.ui.screens.calendar_access

import androidx.lifecycle.ViewModel
import com.example.chooseu.navigation.components.navmanagers.AppNavManager
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject


@HiltViewModel(
    assistedFactory = CalendarSettingViewModel.CalendarSettingFactory::class,
)
class CalendarSettingViewModel @AssistedInject constructor(
    @Assisted private val userId: String,
    private val navManager: AppNavManager,
) : ViewModel() {


@AssistedFactory
 interface  CalendarSettingFactory{

     fun create(userId: String): CalendarSettingViewModel
}


    fun getUserId(): String = userId

    fun onBackPress() {
//        navManager.navigate(
//            BottomNavBarDestinations.Profile
//        )
    }


}