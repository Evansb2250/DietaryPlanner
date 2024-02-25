package com.example.chooseu.repo

sealed class AuthorizationResponseStates {
    data class SuccessResponseState(
        val email: String,
        val name: String,
    ) : AuthorizationResponseStates()

    data class FirstTimeUserState(
        val email: String,
        val name: String = "",
    ) : AuthorizationResponseStates()

    data class FailedResponsState(
        val message: String,
    ) : AuthorizationResponseStates()
}