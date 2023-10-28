package com.example.googlelightcalendar.core.viewmodels.login

import assertk.assertThat
import assertk.assertions.isEqualTo
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.MethodSource
import java.util.stream.Stream

class LoginScreenStatesTest {
    data class LoginScreenStatesArgs(
        val userName: String,
        val password: String,
        val expectedIsValidEmailState: Boolean,
        val expectedIsValidPasswordState: Boolean,
        val expectedContainsValidCredentialState: Boolean,
    )


    @MethodSource("providesLoginScreenStatesArgs")
    @ParameterizedTest
    fun loginScreenstateTest(
        loginScreenStates: LoginScreenStatesArgs,
    ) {
        val state =
            LoginScreenStates.LoginScreenState(
                initialUserName = loginScreenStates.userName,
                initialPassword = loginScreenStates.password,
            )

        val isValidEmail = state.isValidEmail()
        val isValidPassword = state.isValidPassword()
        val containsValidCredentialState = state.containsValidCredentials()

        assertThat(isValidEmail).isEqualTo(loginScreenStates.expectedIsValidEmailState)

        assertThat(isValidPassword).isEqualTo(loginScreenStates.expectedIsValidPasswordState)

        assertThat(containsValidCredentialState).isEqualTo(loginScreenStates.expectedContainsValidCredentialState)

    }

    @Test
    fun containsLoginErrorTest() {
        val state =
            LoginScreenStates.LoginScreenState(
                error = LoginScreenStates.LoginError(
                    "Network error"
                )
            )

        assertThat(state.isLoginError).isEqualTo(true)
    }

    @Test
    fun doesntContainLoginErrorTest() {
        val state = LoginScreenStates.LoginScreenState()

        assertThat(state.isLoginError).isEqualTo(false)
    }


    companion object {
        @JvmStatic
        fun providesLoginScreenStatesArgs() = Stream.of(
            //Invalid email and invalid password
            LoginScreenStatesArgs(
                userName = "Sads",
                password = "ssssssss",
                expectedIsValidEmailState = false,
                expectedIsValidPasswordState = false,
                expectedContainsValidCredentialState = false,
            ),
            //Valid email and invalid password
            LoginScreenStatesArgs(
                userName = "example@gmail.com",
                password = "ssssssss",
                expectedIsValidEmailState = true,
                expectedIsValidPasswordState = false,
                expectedContainsValidCredentialState = false,
            ),
            //Invalid email and Valid password
            LoginScreenStatesArgs(
                userName = "Sads",
                password = "okdSEmqoa9",
                expectedIsValidEmailState = false,
                expectedIsValidPasswordState = true,
                expectedContainsValidCredentialState = false,
            ),
            //Valid email and Valid password
            LoginScreenStatesArgs(
                userName = "example@gmail.com",
                password = "okdSEmqoa9",
                expectedIsValidEmailState = true,
                expectedIsValidPasswordState = true,
                expectedContainsValidCredentialState = true,
            ),
        )

    }


}