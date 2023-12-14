package com.example.googlelightcalendar.core.registration

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.example.googlelightcalendar.navigation.components.NavigationDestinations.registerPhysicalScreen
import com.example.googlelightcalendar.navigation.components.NavigationManger
import com.example.googlelightcalendar.utils.TextFieldUtils
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class RegistrationViewModel @Inject constructor(
    val registrationCache: UserRegistrationCache,
    val navigationManger: NavigationManger,
) : ViewModel() {

    private var _state: MutableStateFlow<InitialRegistrationState> =
        MutableStateFlow(
            InitialRegistrationState.PersonalInformationState()
        )
    val state = _state.asStateFlow()

    init {
        _state.value = InitialRegistrationState.PersonalInformationState()
    }

    fun onStoreCredentials(
        state: InitialRegistrationState.PersonalInformationState
    ) {
        if (state.registrationComplete()) {
            registrationCache.storeKey(RegistrationKeys.FirstName, state.firstName.value)
            registrationCache.storeKey(RegistrationKeys.LASTNAME, state.lastName.value)
            registrationCache.storeKey(RegistrationKeys.EMAIL, state.email.value)
            registrationCache.storeKey(RegistrationKeys.PASSWORD, state.password.value)

            navigateNextPage()
        } else {
            _state.value = InitialRegistrationState.Failed("Form not completed")
        }
    }

    private fun navigateNextPage() {
        navigationManger.navigate(registerPhysicalScreen)
    }
    fun reset() {
        // reset needed
        _state.value = InitialRegistrationState.PersonalInformationState()
    }
}


sealed class InitialRegistrationState : RegistrationScreenStates() {
    data class PersonalInformationState(
        private val initialFirstName: String = "",
        private val initialLastName: String = "",
        private val initialEmail: String = "",
        private val initialPassword: String = "",
    ) : InitialRegistrationState() {

        var firstName = mutableStateOf(initialFirstName)
        var lastName = mutableStateOf(initialLastName)
        var email = mutableStateOf(initialEmail)
        var password = mutableStateOf(initialPassword)


        fun containsValidFirstName(): Boolean {
            return TextFieldUtils.isValidName(firstName.value)
        }

        fun containsValidLastName(): Boolean {
            return TextFieldUtils.isValidName(lastName.value)
        }

        fun containsValidEmail(): Boolean {
            return TextFieldUtils.isValidEmail(email.value)
        }

        fun containsValidPassword(): Boolean {
            return TextFieldUtils.isValidPassword(password.value)
        }

        fun registrationComplete(): Boolean {
            return containsValidEmail() &&
                    containsValidPassword() &&
                    containsValidLastName() &&
                    containsValidFirstName()
        }
    }

    data class Failed(
        val errorMessage: String,
    ) : InitialRegistrationState()

}

