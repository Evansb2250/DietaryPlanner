package com.example.googlelightcalendar.core.viewmodels.login

import androidx.compose.runtime.mutableStateOf

sealed class LoginScreenStates {
    data class LoginScreenState(
        private val initialUserName: String = "",
        private val initialPassword: String = "",
        val loggedInSuccessfully: Boolean = false,
        val isLoading: Boolean = false,
        val error: LoginError? = null,
    ) : LoginScreenStates() {
        var userName = mutableStateOf(initialUserName)
        var password = mutableStateOf(initialPassword)
        val isLoginError: Boolean = containsLoginError()

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

        private fun containsLoginError(): Boolean {
            return error != null
        }
    }
    data class LoginError(val message: String) : LoginScreenStates()
}