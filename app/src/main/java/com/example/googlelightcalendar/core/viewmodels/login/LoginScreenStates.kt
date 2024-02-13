package com.example.googlelightcalendar.core.viewmodels.login

import com.example.googlelightcalendar.utils.TextFieldUtils

sealed class LoginScreenStates {
    data class LoginScreenState(
        val email: String = "",
        val password: String = "",
        val containsIncompleteCredentials: Boolean = false,
    ) : LoginScreenStates() {

        fun containsValidCredentials(): Boolean {
            return isValidPassword() && isValidEmail()
        }

        fun isValidPassword(): Boolean {
            //checks to see if it contains the same letters
            return TextFieldUtils.isValidPassword(password = password)
        }

        fun isValidEmail(): Boolean {
            return TextFieldUtils.isValidEmail(email)
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