package com.example.chooseu.ui.screens.login_screen

import com.example.chooseu.utils.TextFieldUtils
import com.example.chooseu.utils.TexFieldState
import com.example.chooseu.utils.TextFieldUtils.validateAllFields

sealed class LoginScreenStates {
    data class LoginScreenState(
        val email: String = "",
        val password: String = "",
        val containsIncompleteCredentials: Boolean = false,
    ) : LoginScreenStates() {

        fun containsValidCredentials(): TexFieldState {
            return validateAllFields(
                listOf(
                    isValidEmail(),
                    isValidPassword()
                )
            )
        }

        fun isValidPassword(): TexFieldState {
            //checks to see if it contains the same letters
            return TextFieldUtils.isValidPassword(password = password)
        }

        fun isValidEmail(): TexFieldState {
            return TextFieldUtils.isValidEmail(email)
        }
    }

    object Loading : LoginScreenStates()
    data class RegistrationRequiredState(
        val email: String,
    ) : LoginScreenStates()

    data class UserSignedInState(val userId: String): LoginScreenStates()

    data class LoginError(val message: String) : LoginScreenStates()
}