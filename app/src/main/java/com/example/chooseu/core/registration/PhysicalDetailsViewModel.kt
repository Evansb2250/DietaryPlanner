package com.example.chooseu.core.registration

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.chooseu.core.registration.cache.keys.RegistrationKeys
import com.example.chooseu.core.registration.cache.UserRegistrationCache
import com.example.chooseu.core.registration.state.ErrorState
import com.example.chooseu.core.registration.state.PhysicalDetailState
import com.example.chooseu.navigation.components.destinations.GeneralDestinations
import com.example.chooseu.navigation.components.navmanagers.AuthNavManager
import com.example.chooseu.repo.UserRepository
import com.example.chooseu.utils.AsyncResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class PhysicalDetailsViewModel @Inject constructor(
    val navigationManger: AuthNavManager,
    private val cache: UserRegistrationCache,
    private val userRepository: UserRepository,
) : ViewModel() {

    private val _state: MutableStateFlow<PhysicalDetailState> = MutableStateFlow(PhysicalDetailState.PhysicalDetails())
    val state = _state.asStateFlow()

    fun storePhysicalDetailsInCache(
        data: PhysicalDetailState.PhysicalDetails
    ) {
        if (data.completedForm()) {
            cache.storeKey(RegistrationKeys.GENDER, data.selectedGender.value.gender)
            cache.storeKey(RegistrationKeys.BIRTHDATE, data.birthDate.value)
            cache.storeKey(RegistrationKeys.HEIGHT, data.userHeight.value.height)
            cache.storeKey(RegistrationKeys.HEIGHT_METRIC, data.userHeight.value.heightType.type)
            cache.storeKey(RegistrationKeys.WEIGHT, data.userWeight.value.weight)
            cache.storeKey(RegistrationKeys.WEIGHTUNIT, data.userWeight.value.weightType.type)

            createAccount()
        } else {
            _state.value = PhysicalDetailState.PhysicalDetails(
                errorState = ErrorState(
                    isError = true,
                    message = "Missing information!! please complete form."
                )
            )
        }
    }

    fun createAccount() {
        _state.update { 
            PhysicalDetailState.Loading
        }
        viewModelScope.launch {
            val result = userRepository.createUser(cache.getCache())
            when (result) {
                is AsyncResponse.Failed -> {
                    _state.update {
                        PhysicalDetailState.PhysicalDetails(
                            errorState = ErrorState(
                                isError = true,
                                message = result.message
                            )
                        )
                    }
                }

                is AsyncResponse.Success -> {
                    navigateToLoginScreen()
                }
            }
        }
    }

    fun navigateToLoginScreen() {
        navigationManger.navigate(
            GeneralDestinations.OnAppStartUpDestination
        )
        // removes all the data
        cache.clearCache()
    }

    fun reset() {
        _state.value = PhysicalDetailState.PhysicalDetails()
    }

}
