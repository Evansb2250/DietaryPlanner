package com.example.googlelightcalendar.core.registration

import androidx.lifecycle.ViewModel
import com.example.googlelightcalendar.core.registration.RegistrationScreenStates.*
import com.example.googlelightcalendar.core.registration.RegistrationScreenStates.RegistrationStatesPageOne.*
import com.example.googlelightcalendar.navigation.components.NavigationDestinations.registerPhysicalScreen
import com.example.googlelightcalendar.navigation.components.NavigationManger
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class RegistrationViewModel @Inject constructor(
    val registrationCache: UserRegistrationCache,
    val navigationManger: NavigationManger,
) : ViewModel() {
    private var _state: MutableStateFlow<RegistrationStatesPageOne> =
        MutableStateFlow<RegistrationStatesPageOne>(
            PersonalInformationState()
        )
    val state = _state.asStateFlow()

    init {
        _state.value = PersonalInformationState()
    }

    fun onStoreCredentials(
        state: PersonalInformationState
    ) {
        registrationCache.storeKey(RegistrationKeys.FirstName, state.firstName.value)
        registrationCache.storeKey(RegistrationKeys.LASTNAME, state.lastName.value)
        registrationCache.storeKey(RegistrationKeys.EMAIL, state.email.value)
        registrationCache.storeKey(RegistrationKeys.PASSWORD, state.password.value)

        navigateNextPage()
    }

    fun navigateNextPage() {
        navigationManger.navigate(registerPhysicalScreen)
    }

    fun reset(){
        // reset needed
        _state.value = PersonalInformationState()
    }

    fun onBackSpace() {
    }

}