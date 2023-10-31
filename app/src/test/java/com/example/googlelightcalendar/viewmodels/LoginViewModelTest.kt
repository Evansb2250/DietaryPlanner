package com.example.googlelightcalendar.viewmodels

import android.content.Intent
import androidx.activity.result.ActivityResultLauncher
import app.cash.turbine.test
import assertk.assertThat
import assertk.assertions.isEqualTo
import com.example.googlelightcalendar.core.viewmodels.login.LoginViewModel
import com.example.googlelightcalendar.data.room.database.dao.UserDao
import com.example.googlelightcalendar.fakes.OAuthClientFake
import com.example.googlelightcalendar.fakes.UserDaoFake
import com.example.googlelightcalendar.fakes.UserRepositoryFake
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.mockito.kotlin.mock

@RunWith(JUnit4::class)
class LoginViewModelTest {

    private lateinit var userRespositoryFake: UserRepositoryFake
    private lateinit var oauthClientFake: OAuthClientFake
    private lateinit var loginViewModel: LoginViewModel
    private lateinit var userDaoFake: UserDao

    @BeforeEach
    fun setUp() {
        userDaoFake = UserDaoFake()
        oauthClientFake = OAuthClientFake()

        userRespositoryFake = UserRepositoryFake(
            oauthClientFake,
            userDaoFake
        )

        loginViewModel = LoginViewModel(
            userRepository = userRespositoryFake
        )
    }


    @Test
    fun signIn() = runBlocking {
        val mockedLauncher = mock<ActivityResultLauncher<Intent>>()
        loginViewModel.state.test {
            //Initial
            val loadingState = awaitItem()

            assertThat(loadingState.isLoading).isEqualTo(true)

            assertThat(oauthClientFake.attemptToAuthorize).isEqualTo(false)

           loginViewModel.registerAuthLauncher(mockedLauncher)

            loginViewModel.signInWithGoogle()

           assertThat(oauthClientFake.attemptToAuthorize).isEqualTo(true)

            loginViewModel.handleAuthorizationResponse(mock())


            val nextState = awaitItem()

            assertThat(nextState.loggedInSuccessfully).isEqualTo(true)
        }
    }

    @Test
    fun createAccountManually() {

    }

    @Test
    fun attemptLogin() {

    }

    @Test
    fun verifyCorrectEmailAddress() {

    }

    @Test
    fun verifyCorrectPasswordTest() {

    }

    @Test
    fun signInWithFaceBook() {

    }

    @Test
    fun signInWithGoogle() {

    }

}