package com.example.chooseu.core.viewmodels.login

import com.example.chooseu.utils.TextFieldUtils
import com.example.chooseu.utils.TextVerificationStates

sealed class LoginScreenStates {
    data class LoginScreenState(
        val email: String = "",
        val password: String = "",
        val containsIncompleteCredentials: Boolean = false,
    ) : LoginScreenStates() {

        fun containsValidCredentials(): TextVerificationStates {
            return listOf(
                isValidEmail(),
                isValidPassword()
            ).firstOrNull { it is TextVerificationStates.Invalid } ?: TextVerificationStates.Passed
        }

        fun isValidPassword(): TextVerificationStates {
            //checks to see if it contains the same letters
            return TextFieldUtils.isValidPassword(password = password)
        }

        fun isValidEmail(): TextVerificationStates {
            return TextFieldUtils.isValidEmail(email)
        }
    }

    object Loading : LoginScreenStates()
    data class RegistrationRequiredState(
        val email: String,
    ) : LoginScreenStates()

    data class UserSignedInState(
        val email: String,
        val name: String,
    ) : LoginScreenStates()

    data class LoginError(val message: String) : LoginScreenStates()
}