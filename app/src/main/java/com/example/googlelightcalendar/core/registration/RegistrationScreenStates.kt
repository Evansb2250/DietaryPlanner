package com.example.googlelightcalendar.core.registration

sealed class RegistrationScreenStates {
    object GoalSelectionState : RegistrationScreenStates()
    object ConfirmationState : RegistrationScreenStates()

}


enum class Genders {
    MALE,
    FEMALE,
    OTHER,
}