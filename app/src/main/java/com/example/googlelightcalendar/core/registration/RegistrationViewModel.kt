package com.example.googlelightcalendar.core.registration

import androidx.lifecycle.ViewModel
import com.example.googlelightcalendar.core.registration.RegistrationScreenStates.*
import com.example.googlelightcalendar.core.registration.RegistrationScreenStates.RegistrationStatesPageOne.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class RegistrationViewModel @Inject constructor() : ViewModel() {
    private var _state: MutableStateFlow<RegistrationStatesPageOne> = MutableStateFlow<RegistrationStatesPageOne>(
        PersonalInformationState()
    )
    val state = _state.asStateFlow()

}