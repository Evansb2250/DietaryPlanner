package com.example.googlelightcalendar.viewmodels

import android.content.ActivityNotFoundException
import android.content.Intent
import androidx.activity.result.ActivityResultLauncher
import app.cash.turbine.test
import assertk.assertThat
import assertk.assertions.isEqualTo
import assertk.assertions.isInstanceOf
import com.example.googlelightcalendar.core.viewmodels.login.LoginScreenStates
import com.example.googlelightcalendar.core.viewmodels.login.LoginViewModel
import com.example.googlelightcalendar.data.room.database.dao.UserDao
import com.example.googlelightcalendar.data.room.database.models.UserEntity
import com.example.googlelightcalendar.domain.User
import com.example.googlelightcalendar.domain.toUserEntity
import com.example.googlelightcalendar.fakes.OAuthClientFake
import com.example.googlelightcalendar.fakes.UserDaoFake
import com.example.googlelightcalendar.fakes.UserRepositoryFake
import com.example.googlelightcalendar.navigation.components.NavigationBuilder
import com.example.googlelightcalendar.navigation.components.NavigationDestinations
import com.example.googlelightcalendar.navigation.components.NavigationManger
import com.example.googlelightcalendar.repo.UserRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.mockito.Mockito
import org.mockito.kotlin.any
import org.mockito.kotlin.mock
import org.mockito.kotlin.times
import org.mockito.kotlin.verify

@OptIn(kotlinx.coroutines.ExperimentalCoroutinesApi::class)
class LoginViewModelTest {

    private lateinit var externalScope: CoroutineScope
    private lateinit var userRespositoryFake: UserRepository
    private lateinit var navigationManager: NavigationManger
    private lateinit var oauthClientFake: OAuthClientFake
    private lateinit var loginViewModel: LoginViewModel
    private lateinit var userDaoFake: UserDao

    private val mockedLauncher = mock<ActivityResultLauncher<Intent>>()

    private suspend fun setUpDependencies(
        emailInRoom: String = "",
        gmailFoundDuringOauth: String? = null,
    ) {

        userDaoFake.insertUser(
            User(
                userName = emailInRoom,
                name = ""
            ).toUserEntity()
        )

        oauthClientFake.setGmailAccount(gmailFoundDuringOauth)
    }


    @BeforeEach
    fun setUp() {
        val testDispatcher = StandardTestDispatcher()

        Dispatchers.setMain(testDispatcher)

        externalScope = mock()

        navigationManager = mock()

        userDaoFake = UserDaoFake()
        oauthClientFake = OAuthClientFake()

        userRespositoryFake = UserRepositoryFake(
            oauthClientFake,
            userDaoFake
        )

        loginViewModel = LoginViewModel(
            navigationManager = navigationManager,
            userRepository = userRespositoryFake,
            dispatcher = testDispatcher
        )
    }

    @After
    fun tearDown() {
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
    fun `Sign in with Google successful`() = runTest {
        setUpDependencies(
            "sam",
            "sam"
        )

        loginViewModel.state.test {
            //Initial
            val loginScreenState: LoginScreenStates = awaitItem()

            assertThat(loginScreenState).isInstanceOf<LoginScreenStates.LoginScreenState>()

            assertThat(oauthClientFake.attemptToAuthorize).isEqualTo(false)

            loginViewModel.registerAuthLauncher(mockedLauncher)

            loginViewModel.signInWithGoogle()

            assertThat(oauthClientFake.attemptToAuthorize).isEqualTo(true)

            loginViewModel.handleAuthorizationResponse(mock())


            val nextState = awaitItem()

            assertThat(nextState).isInstanceOf<LoginScreenStates.UserSignedInState>()
        }
    }


    @Test
    fun `Sign in with Google Failed due to cancelling`() = runTest {
        loginViewModel.state.test {
            //Initial
            val loginScreenState = awaitItem()

            assertThat(loginScreenState).isInstanceOf<LoginScreenStates.LoginScreenState>()

            assertThat(oauthClientFake.attemptToAuthorize).isEqualTo(false)

            loginViewModel.registerAuthLauncher(mockedLauncher)

            loginViewModel.signInWithGoogle()

            assertThat(oauthClientFake.attemptToAuthorize).isEqualTo(true)

            //Simulates that the user cancels the process of getting the token before retrieved
            oauthClientFake.cancelOauthProcess()

            //call back is received once we are back in the OnCreate function of our activity.
            loginViewModel.handleAuthorizationResponse(mock())


            val nextState = awaitItem()

            assertThat(nextState).isInstanceOf<LoginScreenStates.LoginError>()

            //reset state to attempt to log in aagin.
            loginViewModel.resetLoginScreenState()

            val thirdState: LoginScreenStates = awaitItem()

            assertThat(thirdState).isInstanceOf<LoginScreenStates.LoginScreenState>()
        }
    }

    @Test
    fun `sign in manually Failed  `() = runTest {
        loginViewModel.state.test {
            awaitItem()

            userDaoFake.insertUser(
                UserEntity("Sam", "", lastName = "", password = "132")
            )

            loginViewModel.signInManually("Sam", "1232")

            val loginViewModelState = awaitItem()

            assertThat(loginViewModelState).isInstanceOf<LoginScreenStates.LoginError>()
        }
    }

    @Test
    fun navigateToRegisterScreen() {

        verify(navigationManager, times(0)).navigate(
            NavigationDestinations.registerScreen
        )

        loginViewModel.navigateToRegisterScreen()

        verify(navigationManager, times(1)).navigate(
            any()
        )
    }

    @Test
    fun `sign in manually Successfully  `() = runTest {
        loginViewModel.state.test {
            awaitItem()

            userDaoFake.insertUser(
                UserEntity("Sam", "", lastName = "", password = "1232")
            )

            loginViewModel.signInManually("Sam", "1232")

            val loginViewModelState = awaitItem()

            assertThat(loginViewModelState).isInstanceOf<LoginScreenStates.UserSignedInState>()

        }
    }
}