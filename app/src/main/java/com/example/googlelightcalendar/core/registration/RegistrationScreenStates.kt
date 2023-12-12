package com.example.googlelightcalendar.core.registration

import androidx.compose.runtime.mutableStateOf
import com.example.googlelightcalendar.utils.TextFieldUtils
import java.util.Date

sealed class RegistrationScreenStates {

    sealed class RegistrationStatesPageOne : RegistrationScreenStates() {
        data class PersonalInformationState(
            private val initialFirstName: String = "",
            private val initialLastName: String = "",
            private val initialEmail: String = "",
            private val initialPassword: String = "",
        ) : RegistrationStatesPageOne() {

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
        ) : RegistrationStatesPageOne()

    }

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

    object GoalSelectionState : RegistrationScreenStates()
    object ConfirmationState : RegistrationScreenStates()

}


enum class Genders {
    MALE,
    FEMALE,
    OTHER,
}