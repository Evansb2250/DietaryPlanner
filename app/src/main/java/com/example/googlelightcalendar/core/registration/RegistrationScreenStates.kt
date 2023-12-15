package com.example.googlelightcalendar.core.registration

sealed class RegistrationScreenStates {
    object GoalSelectionState : RegistrationScreenStates()
    object ConfirmationState : RegistrationScreenStates()

}


sealed class Genders(val gender:String) {
    object Male: Genders(gender = "Male")
    object FEMALE: Genders(gender = "Female")
    object OTHER: Genders(gender = "Other")
}