package com.example.googlelightcalendar.viewmodels

import android.content.Intent
import androidx.activity.result.ActivityResultLauncher
import assertk.assertThat
import assertk.assertions.isEqualTo
import com.example.googlelightcalendar.core.viewmodels.login.LoginViewModel
import com.example.googlelightcalendar.data.room.database.dao.UserDao
import com.example.googlelightcalendar.fakes.OAuthClientFake
import com.example.googlelightcalendar.fakes.UserDaoFake
import com.example.googlelightcalendar.fakes.UserRepositoryFake
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
    fun signIn() {
        val mockedLauncher = mock<ActivityResultLauncher<Intent>>()

        assertThat(oauthClientFake.attemptToAuthorize).isEqualTo(false)
        loginViewModel.registerAuthLauncher(mockedLauncher)
        loginViewModel.signInWithGoogle()
        assertThat(oauthClientFake.attemptToAuthorize).isEqualTo(true)
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