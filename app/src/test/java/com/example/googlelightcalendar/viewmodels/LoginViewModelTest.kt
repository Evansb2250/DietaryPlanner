package com.example.googlelightcalendar.viewmodels

import android.content.ActivityNotFoundException
import android.content.Intent
import androidx.activity.result.ActivityResultLauncher
import app.cash.turbine.test
import assertk.assertThat
import assertk.assertions.isEqualTo
import assertk.assertions.isNotNull
import com.example.googlelightcalendar.auth.OauthClient
import com.example.googlelightcalendar.auth.OauthClientImp
import com.example.googlelightcalendar.auth.Token
import com.example.googlelightcalendar.core.GoogleTokenManagerImpl
import com.example.googlelightcalendar.core.TokenManager
import com.example.googlelightcalendar.core.viewmodels.login.LoginViewModel
import com.example.googlelightcalendar.data.room.database.dao.UserDao
import com.example.googlelightcalendar.data.room.database.models.UserEntity
import com.example.googlelightcalendar.fakes.OAuthClientFake
import com.example.googlelightcalendar.fakes.UserDaoFake
import com.example.googlelightcalendar.fakes.UserRepositoryFake
import com.example.googlelightcalendar.repo.UserRepository
import com.example.googlelightcalendar.repo.UserRepositoryImpl
import com.example.googlelightcalendar.utils.AsyncResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.mockito.internal.matchers.Any
import org.mockito.internal.verification.Times
import org.mockito.kotlin.any
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever
import org.mockito.verification.VerificationMode
import kotlin.jvm.Throws

class LoginViewModelTest {


    private lateinit var userRespositoryFake: UserRepository
    private lateinit var oauthClientFake: OAuthClientFake
    private lateinit var loginViewModel: LoginViewModel
    private lateinit var userDaoFake: UserDao

    private val mockedLauncher = mock<ActivityResultLauncher<Intent>>()

    @BeforeEach
    fun setUp() {
        val testDispatcher = StandardTestDispatcher()

        Dispatchers.setMain(testDispatcher)

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

    @After
    fun tearDown(){
        Dispatchers.resetMain()
    }


    @Test
    fun `registered ActivityResult Launcher Test`() {
        assertThat(oauthClientFake.isRegistered).isEqualTo(false)
        loginViewModel.registerAuthLauncher(mockedLauncher)
        assertThat(oauthClientFake.isRegistered).isEqualTo(true)
    }

    @Test
    fun `launch Sign In with Google Method Success`() {
        assertThat(oauthClientFake.isRegistered).isEqualTo(false)
        loginViewModel.registerAuthLauncher(mockedLauncher)
        assertThat(oauthClientFake.isRegistered).isEqualTo(true)
        loginViewModel.signInWithGoogle()
        assertThat(oauthClientFake.attemptToAuthorize).isEqualTo(true)
    }

    @Test
    fun `failed Sign in with google Method  authorizationLauncher no initialized Throws Exception `() {
        assertThrows<ActivityNotFoundException> {
            loginViewModel.signInWithGoogle()
        }
    }

    @Test
    fun `Sign in with Google successful`() = runBlocking {
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
    fun `Sign in with Google Failed`() = runBlocking {
        loginViewModel.state.test {
            //Initial
            val loadingState = awaitItem()

            assertThat(loadingState.isLoading).isEqualTo(true)

            assertThat(oauthClientFake.attemptToAuthorize).isEqualTo(false)

            loginViewModel.registerAuthLauncher(mockedLauncher)

            loginViewModel.signInWithGoogle()

            assertThat(oauthClientFake.attemptToAuthorize).isEqualTo(true)

            //Simulates that the user cancels the process of getting the token before retrieved
            oauthClientFake.cancelOauthProcess()

            //call back is received once we are back in the OnCreate function of our activity.
            loginViewModel.handleAuthorizationResponse(mock())


            val nextState = awaitItem()

            assertThat(nextState.loggedInSuccessfully).isEqualTo(false)
            assertThat(nextState.isLoginError).assertThat(true)
            assertThat(nextState.error).isNotNull()

            //reset state to attempt to log in aagin.
            loginViewModel.resetLoginScreenState()

            val thirdState = awaitItem()

            assertThat(thirdState.isLoginError).assertThat(false)
            assertThat(thirdState.isLoading).assertThat(true)

        }
    }

    @Test
    fun `handleAuthorizationResponse`() = runBlocking {
        val intentMock = mock<Intent>()
        loginViewModel.state.test {
            //Initial
            val loadingState = awaitItem()

            assertThat(loadingState.isLoading).isEqualTo(true)

            assertThat(oauthClientFake.attemptToAuthorize).isEqualTo(false)

            loginViewModel.registerAuthLauncher(mockedLauncher)

            loginViewModel.signInWithGoogle()

            assertThat(oauthClientFake.attemptToAuthorize).isEqualTo(true)

            //call back is received once we are back in the OnCreate function of our activity.
            loginViewModel.handleAuthorizationResponse(mock())

            val nextState = awaitItem()

            assertThat(nextState.loggedInSuccessfully).isEqualTo(true)

        }

    }

    @Test
    fun `sign in manually Failed  `() = runTest {
        loginViewModel.state.test {
            awaitItem()

            userDaoFake.insertUser(
                UserEntity("Sam", "", password = "132")
            )

            loginViewModel.signInManually("Sam", "1232")

            val flowResult1 = awaitItem()

            assertThat(flowResult1.loggedInSuccessfully).isEqualTo(false)

        }
    }

    @Test
    fun `sign in manually Successfully  `() = runTest {
        loginViewModel.state.test {
            awaitItem()

            userDaoFake.insertUser(
                UserEntity("Sam", "", password = "1232")
            )

            loginViewModel.signInManually("Sam", "1232")

            val flowResult1 = awaitItem()

            assertThat(flowResult1.loggedInSuccessfully).isEqualTo(true)

        }
    }


}