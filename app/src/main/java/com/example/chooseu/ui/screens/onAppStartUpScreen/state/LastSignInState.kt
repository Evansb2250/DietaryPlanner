package com.example.chooseu.ui.screens.onAppStartUpScreen.state

sealed class LastSignInState {
    data class AlreadyLoggedIn(val userId: String) : LastSignInState()
    object NotLoggedIn : LastSignInState()
}