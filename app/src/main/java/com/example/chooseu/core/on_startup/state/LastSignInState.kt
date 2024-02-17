package com.example.chooseu.core.on_startup.state

sealed class LastSignInState {
    data class AlreadyLoggedIn(val userId: String) : LastSignInState()
    object NotLoggedIn : LastSignInState()
}