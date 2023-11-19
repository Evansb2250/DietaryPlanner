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
    fun failsValidCredentialsTest() {
        val state =
            LoginScreenStates.LoginScreenState(
                initialUserName = "das",
                initialPassword = "ds"
            )

        assertThat(state.containsValidCredentials()).isEqualTo(false)
    }

    @Test
    fun passesValidCredentialsTest() {
        val state = LoginScreenStates.LoginScreenState(
            "example23@gmail.com",
            "examplePassword23"
        )
        assertThat(state.containsValidCredentials()).isEqualTo(true)
    }

    @MethodSource("providesFailedPasswordArgs")
    @ParameterizedTest
    fun isValidPasswordTest(
        args: PasswordCredentials,
        ){
        val loginstate = LoginScreenStates.LoginScreenState(
            initialPassword = args.password
        )

        assertThat(loginstate.isValidPassword()).isEqualTo(args.expectedResult)
    }


    @MethodSource("providesEmailCredentials")
    @ParameterizedTest
    fun isValidEmailFails(
        args: EmailCredentials,
    ){
        val loginState = LoginScreenStates.LoginScreenState(
            initialUserName = args.emailCredential,
        )

        assertThat(loginState.isValidEmail()).isEqualTo(args.expectedResult)
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

        @JvmStatic
        fun providesFailedPasswordArgs() : Stream<PasswordCredentials> = Stream.of(
            PasswordCredentials(
                "Dsaa",
                false,
            ),
            PasswordCredentials(
                "Dsadsa@",
                true,
            ),
            PasswordCredentials(
                "ds@gmail",
                true,
            ),

            PasswordCredentials(
                "aaaaaa",
                false,
            ),
        )

        @JvmStatic
        fun providesEmailCredentials(): Stream<EmailCredentials> = Stream.of(
            EmailCredentials(
                "Dsads",
                false,
            ),
            EmailCredentials(
                "Dsadsa@",
                false,
            ),
            EmailCredentials(
                "Dsadsa@d",
                false,
            ),
            EmailCredentials(
                "Dsadsa@com",
                false,
            ),
            EmailCredentials(
                "Dsadsa@gmail",
                false,
            ),
            EmailCredentials(
                "Dsadsa@gmail.com",
                true,
            ),




            )

    }

    data class EmailCredentials(
        val emailCredential: String,
        val expectedResult: Boolean,
    )
    data class PasswordCredentials(
        val password: String,
        val expectedResult: Boolean,
    )
}