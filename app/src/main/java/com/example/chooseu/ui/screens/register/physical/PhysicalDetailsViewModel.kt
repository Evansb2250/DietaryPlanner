package com.example.chooseu.ui.screens.register.physical

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.chooseu.core.dispatcher_provider.DispatcherProvider
import com.example.chooseu.core.cache.UserRegistrationCache
import com.example.chooseu.core.cache.keys.RegistrationKeys
import com.example.chooseu.ui.screens.register.physical.states.ErrorState
import com.example.chooseu.ui.screens.register.physical.states.PhysicalDetailState
import com.example.chooseu.navigation.components.destinations.GeneralDestinations
import com.example.chooseu.navigation.components.navmanagers.AuthNavManager
import com.example.chooseu.repo.UpdateResult
import com.example.chooseu.repo.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class PhysicalDetailsViewModel @Inject constructor(
    private val navigationManger: AuthNavManager,
    private val cache: UserRegistrationCache,
    private val userRepository: UserRepository,
    private val dispatcherProvider: DispatcherProvider,
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

    private fun createAccount() {
        setStateToLoading()
        viewModelScope.launch(dispatcherProvider.main) {
            val updateResult = userRepository.createUserInServer(cache.getCache())

            when (updateResult) {
                is UpdateResult.Failed -> {
                    _state.update {
                        PhysicalDetailState.PhysicalDetails(
                            errorState = ErrorState(
                                isError = true,
                                message = updateResult.message
                            )
                        )
                    }
                }

                is UpdateResult.Success -> {
                    navigateToLoginScreen()
                }
            }
        }
    }

    private fun setStateToLoading() {
        _state.update {
            PhysicalDetailState.Loading
        }
    }

    fun navigateToLoginScreen() {
        navigationManger.navigate(
            GeneralDestinations.AuthentificationFlow
        )
        // removes all the data
        cache.clearCache()
    }

    fun reset() {
        _state.value = PhysicalDetailState.PhysicalDetails()
    }

}
