package com.example.chooseu.ui.screens.register.main.state

import com.example.chooseu.utils.TextFieldUtils
import com.example.chooseu.utils.TexFieldState
import com.example.chooseu.utils.TextFieldUtils.validateAllFields

sealed class InitialRegistrationState {
    data class PersonalInformationState(
        val firstName: String = "",
        val lastName: String = "",
        val email: String = "",
        val password: String = "",
        val failedLoginState: Failed = Failed()
    ) : InitialRegistrationState() {

        fun containsValidFirstName(): TexFieldState {
            return TextFieldUtils.isValidName(firstName.trim())
        }

        fun containsValidLastName(): TexFieldState {
            return TextFieldUtils.isValidName(lastName)
        }

        fun containsValidEmail(): TexFieldState {
            return TextFieldUtils.isValidEmail(email)
        }

        fun containsValidPassword(): TexFieldState {
            return TextFieldUtils.isValidPassword(password)
        }

        fun registrationComplete(): TexFieldState {
            return validateAllFields(
                listOf(
                    containsValidEmail(),
                    containsValidPassword(),
                    containsValidLastName(),
                    containsValidFirstName(),
                )
            )
        }
    }

    data class Failed(
        val isError: Boolean = false,
        val errorMessage: String? = null,
    )

}