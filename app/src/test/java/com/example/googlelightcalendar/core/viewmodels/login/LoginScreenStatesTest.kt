package com.example.googlelightcalendar.core.viewmodels.login

import assertk.assertThat
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.MethodSource
import java.util.stream.Stream

class LoginScreenStatesTest {
    @ParameterizedTest
    @MethodSource("providesLoginScreenStatesArgs")
    fun test(
        loginScreenStates: LoginScreenStatesArgs,
    ) {
        val state = LoginScreenStates.LoginScreenState(
            initialUserName = loginScreenStates.userName,
            initialPassword = loginScreenStates.password,
        )

        val actualVerifiedUserNameState = state.verifyUserNameFormat()
        val actualVerifiedPasswordState = state.verifyPassword()
        //In case the user hasn't place anythin inside their shouldn't show an error.
        val actualShowIfPasswordIsValid = state.isVerifiedPasswordFormat()
        val actualContainsValidCredentialState = state.containsValidCredentials()




    }


    data class LoginScreenStatesArgs(
        val userName: String,
        val password: String,
        val expectedVerifiedUserNameState: Boolean,
        val expectedVerifiedPasswordState: Boolean,
        val expectedShowIfPasswordIsValid: Boolean,
        val expectedContainsValidCredentialState: Boolean,
    )

    companion object {
        @JvmStatic
        fun providesLoginScreenStatesArgs() : Stream<LoginScreenStatesArgs> = Stream.of(

        )

    }


}