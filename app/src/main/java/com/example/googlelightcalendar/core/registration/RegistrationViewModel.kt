package com.example.googlelightcalendar.core.registration

import androidx.lifecycle.ViewModel
import com.example.googlelightcalendar.core.registration.RegistrationScreenStates.*
import com.example.googlelightcalendar.core.registration.RegistrationScreenStates.RegistrationStatesPageOne.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class RegistrationViewModel @Inject constructor(
    val registrationCache: UserRegistrationCache
) : ViewModel() {
    private var _state: MutableStateFlow<RegistrationStatesPageOne> = MutableStateFlow<RegistrationStatesPageOne>(
        PersonalInformationState()
    )
    val state = _state.asStateFlow()

    init{

    }

    fun onStoreCredentials(
       state: PersonalInformationState
    ){
        registrationCache.storeKey(RegistrationKeys.FirstName, state.firstName.value)
        registrationCache.storeKey(RegistrationKeys.LASTNAME, state.lastName.value)
        registrationCache.storeKey(RegistrationKeys.EMAIL, state.email.value)
        registrationCache.storeKey(RegistrationKeys.PASSWORD, state.password.value)

        _state.value =  Success
    }

}