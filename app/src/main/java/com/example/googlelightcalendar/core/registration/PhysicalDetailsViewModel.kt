package com.example.googlelightcalendar.core.registration

import androidx.lifecycle.ViewModel
import com.example.googlelightcalendar.navigation.components.NavigationDestinations.registerGoalsScreen
import com.example.googlelightcalendar.navigation.components.NavigationManger
import dagger.hilt.android.lifecycle.HiltViewModel
import java.util.Date
import javax.inject.Inject

sealed class PhysicalDetailsState : RegistrationScreenStates() {
    data class UserInput(
        val gender: Genders,
        val birthDate: Date? = null,
        val height: Double,
        val weight: Double,
    ) : PhysicalDetailsState()

    data class Error(
        val errorMessage: String,
    ) : PhysicalDetailsState()

    object Success : PhysicalDetailsState()
}


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