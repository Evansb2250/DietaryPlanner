package com.example.chooseu.utils

object TextFieldUtils {
    fun isValidEmail(
        email: String
    ): Boolean {
        val regex = Regex("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}\$")
        return regex.matches(email)
    }

    fun isValidName(
        name: String,
    ) : Boolean {
        return name.length > 3
    }

    fun isValidPassword(
        password: String
    ): Boolean {
        //checks to see if it contains the same letters
        val passwordRegex = Regex("(.)\\1+")
        return password.length > 6 && !passwordRegex.matches(password)
    }

}