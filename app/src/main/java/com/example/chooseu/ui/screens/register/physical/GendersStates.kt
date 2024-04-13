package com.example.chooseu.ui.screens.register.physical


sealed class Genders(val gender: String) {
    object Male : Genders(gender = "Male")
    object FEMALE : Genders(gender = "Female")
    object OTHER : Genders(gender = "Other")
    object UNSPECIFIED : Genders(gender = "Default")
}