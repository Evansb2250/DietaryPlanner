package com.example.googlelightcalendar.core.registration

import androidx.lifecycle.ViewModel
import com.example.googlelightcalendar.navigation.components.NavigationDestinations.registerGoalsScreen
import com.example.googlelightcalendar.navigation.components.NavigationManger
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class PhysicalDetailsViewModel @Inject constructor(
   private val navigationManger: NavigationManger,
   private val cache: UserRegistrationCache,
) : ViewModel() {






    fun navToRegisterGoals(){
        navigationManger.navigate(
            registerGoalsScreen
        )
    }
}