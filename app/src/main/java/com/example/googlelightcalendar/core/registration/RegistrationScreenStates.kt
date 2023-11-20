package com.example.googlelightcalendar.core.registration

import androidx.compose.runtime.mutableStateOf
import com.example.googlelightcalendar.utils.TextFieldUtils

sealed class RegistrationScreenStates {

    sealed class RegistrationStatesPageOne: RegistrationScreenStates(){
        data class PersonalInformationState(
            val initialFirstName: String = "",
            val initialLastName: String = "",
            val initialEmail: String = "",
            val initialPassword: String = "",
        ): RegistrationStatesPageOne(){

            var firstName = mutableStateOf(initialFirstName)
            var lastName =  mutableStateOf(initialLastName)
            var email = mutableStateOf(initialEmail)
            var password = mutableStateOf(initialPassword)


            fun containsValidFirstName(): Boolean{
                return TextFieldUtils.isValidName(firstName.value)
            }

            fun containsValidLastName(): Boolean{
                return TextFieldUtils.isValidName(lastName.value)
            }

            fun containsValidEmail(): Boolean{
                return TextFieldUtils.isValidEmail(email.value)
            }

            fun containsValidPassword(): Boolean{
                return TextFieldUtils.isValidPassword(password.value)
            }
        }
        object Success : RegistrationStatesPageOne()

        data class Failed(
            val errorMessage: String,
        ): RegistrationStatesPageOne()

    }

    object PhysicalDetailsState: RegistrationScreenStates()
    object GoalSelectionState: RegistrationScreenStates()
    object ConfirmationState: RegistrationScreenStates()

}