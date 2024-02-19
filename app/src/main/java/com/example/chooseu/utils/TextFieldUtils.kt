package com.example.chooseu.utils

sealed class TextVerificationStates() {
    object Passed : TextVerificationStates()
    data class Invalid(val errorMessage: String) : TextVerificationStates()
}

object TextFieldUtils {
    fun isValidEmail(
        email: String
    ): TextVerificationStates {
        val regex = Regex("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}\$")
        return if (regex.matches(email)) {
            TextVerificationStates.Passed
        } else {
            TextVerificationStates.Invalid("invalid email")
        }
    }

    fun isValidName(
        name: String,
    ): TextVerificationStates {
        return if (name.length >= 2) {
            TextVerificationStates.Passed
        } else {
            TextVerificationStates.Invalid("Names must be more than 3 characters")
        }
    }

    fun isValidPassword(
        password: String
    ): TextVerificationStates {
        //checks to see if it contains the same letters
        val passwordRegex = Regex("(.)\\1+")
        return if (password.length >= 6 && !passwordRegex.matches(password)){
            TextVerificationStates.Passed
        }else{
            TextVerificationStates.Invalid("Password must be more than 5 characters")
        }


    }

}