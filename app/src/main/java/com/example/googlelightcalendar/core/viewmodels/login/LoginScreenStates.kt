package com.example.googlelightcalendar.core.viewmodels.login

import androidx.compose.runtime.mutableStateOf

sealed class LoginScreenStates {
    data class LoginScreenState(
        private val initialUserName: String = "",
        private val initialPassword: String = "",
    ) : LoginScreenStates() {
        var userName = mutableStateOf(initialUserName)
        var password = mutableStateOf(initialPassword)

        fun containsValidCredentials(): Boolean {
            return isValidPassword() && isValidEmail()
        }

        fun isValidPassword(): Boolean {
            //checks to see if it contains the same letters
            val passwordRegex = Regex("(.)\\1+")
            return password.value.length > 6 && !passwordRegex.matches(password.value)
        }

        fun isValidEmail(): Boolean {
            val regex = Regex("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}\$")
            return regex.matches(userName.value)
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