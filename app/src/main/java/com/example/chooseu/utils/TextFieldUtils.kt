package com.example.chooseu.utils

sealed class TexFieldState() {
    object Passed : TexFieldState()
    data class Invalid(val errorMessage: String) : TexFieldState()
}

object TextFieldUtils {
    fun isValidEmail(
        email: String
    ): TexFieldState {
        val regex = Regex("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}\$")
        return if (regex.matches(email)) {
            TexFieldState.Passed
        } else {
            TexFieldState.Invalid("invalid email")
        }
    }

    fun isValidName(
        name: String,
    ): TexFieldState {
        return if (name.length >= 2) {
            TexFieldState.Passed
        } else {
            TexFieldState.Invalid("Names must be more than 3 characters")
        }
    }

    fun isValidPassword(
        password: String
    ): TexFieldState {
        //checks to see if it contains the same letters
        val passwordRegex = Regex("(.)\\1+")
        return if (password.length >= 6 && !passwordRegex.matches(password)) {
            TexFieldState.Passed
        } else {
            TexFieldState.Invalid("Password must be more than 5 characters")
        }

    }


    fun validateAllFields(listOfRequirements: List<TexFieldState>): TexFieldState {
        val error: String = listOfRequirements.filterIsInstance(TexFieldState.Invalid::class.java).map {
                it.errorMessage
            }.joinToString(separator = "\n") { "* $it" }

        return if (error.isNullOrEmpty()) TexFieldState.Passed else TexFieldState.Invalid(error)
    }

}