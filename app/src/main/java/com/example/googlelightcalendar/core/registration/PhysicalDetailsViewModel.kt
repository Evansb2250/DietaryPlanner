package com.example.googlelightcalendar.core.registration

import androidx.lifecycle.ViewModel
import com.example.googlelightcalendar.core.registration.state.ErrorHolder
import com.example.googlelightcalendar.core.registration.state.PhysicalDetailState
import com.example.googlelightcalendar.navigation.components.NavigationDestinations.registerGoalsScreen
import com.example.googlelightcalendar.navigation.components.NavigationManger
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject


@HiltViewModel
class PhysicalDetailsViewModel @Inject constructor(
    private val navigationManger: NavigationManger,
    private val cache: UserRegistrationCache,
) : ViewModel() {

    private val _state = MutableStateFlow(PhysicalDetailState.PhysicalDetails())
    val state = _state.asStateFlow()

    fun storePhysicalDetailsInCache(
        data: PhysicalDetailState.PhysicalDetails
    ) {
        if (
            data.completedForm()
        ) {
            cache.storeKey(RegistrationKeys.GENDER, data.selectedGender.value.gender)
            cache.storeKey(RegistrationKeys.BIRTHDATE, data.birthDate.value)
            cache.storeKey(RegistrationKeys.HEIGHT, data.userHeight.value.height)
            cache.storeKey(RegistrationKeys.HEIGHTUNIT, data.userHeight.value.heightType.type)
            cache.storeKey(RegistrationKeys.WEIGHT, data.userWeight.value.weight)
            cache.storeKey(RegistrationKeys.WEIGHTUNIT, data.userWeight.value.weightType.type)

            navToRegisterGoals()

            reset()
        } else {
            _state.value = PhysicalDetailState.PhysicalDetails(
                errorState = ErrorHolder(
                    isError = true,
                    message = "Missing information!! please complete form."
                )
            )
        }
    }


    private fun navToRegisterGoals() {
        navigationManger.navigate(
            registerGoalsScreen
        )
    }

    fun reset() {
        _state.value = PhysicalDetailState.PhysicalDetails()
    }

}
