package com.example.googlelightcalendar.core.viewmodels.login

import androidx.compose.runtime.mutableStateOf
import com.example.googlelightcalendar.utils.TextFieldUtils

sealed class LoginScreenStates {
    data class LoginScreenState(
        private val initialUserName: String = "",
        private val initialPassword: String = "",
    ) : LoginScreenStates() {
        var email = mutableStateOf(initialUserName)
        var password = mutableStateOf(initialPassword)

        fun containsValidCredentials(): Boolean {
            return isValidPassword() && isValidEmail()
        }

        fun isValidPassword(): Boolean {
            //checks to see if it contains the same letters
            return TextFieldUtils.isValidPassword(password = password.value)
        }

        fun isValidEmail(): Boolean {
            return TextFieldUtils.isValidEmail(email.value)
        }
    }

    data class RegistrationRequiredState(
        val email: String,
    ) : LoginScreenStates()

    data class UserSignedInState(
        val email: String,
        val name: String,
    ) : LoginScreenStates()

    data class LoginError(val message: String) : LoginScreenStates()
}