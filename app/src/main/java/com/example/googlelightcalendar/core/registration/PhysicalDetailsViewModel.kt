package com.example.googlelightcalendar.core.registration

import androidx.lifecycle.ViewModel
import com.example.googlelightcalendar.core.registration.state.ErrorState
import com.example.googlelightcalendar.core.registration.state.PhysicalDetailState
import com.example.googlelightcalendar.navigation.components.NavigationDestinations
import com.example.googlelightcalendar.navigation.components.navmanagers.AuthNavManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject


@HiltViewModel
class PhysicalDetailsViewModel @Inject constructor(
    val navigationManger: AuthNavManager,
    private val cache: UserRegistrationCache,
) : ViewModel() {

    private val _state = MutableStateFlow(PhysicalDetailState.PhysicalDetails())
    val state = _state.asStateFlow()

    fun storePhysicalDetailsInCache(
        data: PhysicalDetailState.PhysicalDetails
    ) {
        if (true) {
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
                errorState = ErrorState(
                    isError = true,
                    message = "Missing information!! please complete form."
                )
            )
        }
    }


    private fun navToRegisterGoals() {
        navigationManger.navigate(
            navigation = NavigationDestinations.RegisterGoalsScreen,
        )
    }

    fun reset() {
        _state.value = PhysicalDetailState.PhysicalDetails()
    }

}
