package com.example.chooseu.viewmodels

import android.content.ActivityNotFoundException
import android.content.Intent
import androidx.activity.result.ActivityResultLauncher
import app.cash.turbine.test
import assertk.assertThat
import assertk.assertions.isEqualTo
import assertk.assertions.isInstanceOf
import com.example.chooseu.core.viewmodels.login.LoginScreenStates
import com.example.chooseu.core.viewmodels.login.LoginViewModel
import com.example.chooseu.data.database.dao.UserDao
import com.example.chooseu.data.database.models.UserEntity
import com.example.chooseu.domain.CurrentUser
import com.example.chooseu.domain.toUserEntity
import com.example.chooseu.fakes.OAuthClientFake
import com.example.chooseu.fakes.UserDaoFake
import com.example.chooseu.fakes.UserRepositoryFake
import com.example.chooseu.navigation.components.destinations.GeneralDestinations
import com.example.chooseu.repo.UserRepository
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
import org.mockito.kotlin.mock
import org.mockito.kotlin.times
import org.mockito.kotlin.verify

@OptIn(kotlinx.coroutines.ExperimentalCoroutinesApi::class)
class LoginViewModelTest {

    private lateinit var externalScope: CoroutineScope
    private lateinit var userRespositoryFake: UserRepository
    private lateinit var navigationManager: AuthNavManager
    private lateinit var oauthClientFake: OAuthClientFake
    private lateinit var loginViewModel: LoginViewModel
    private lateinit var userDaoFake: UserDao

    private val mockedLauncher = mock<ActivityResultLauncher<Intent>>()

    private suspend fun setUpDependencies(
        emailInRoom: String = "",
        gmailFoundDuringOauth: String? = null,
    ) {

        userDaoFake.insertUser(
            CurrentUser(
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

            loginViewModel.attemptSignIn("Sam", "1232")

            val loginViewModelState = awaitItem()

            assertThat(loginViewModelState).isInstanceOf<LoginScreenStates.LoginError>()
        }
    }

    @Test
    fun navigateHomeScreenTest(){
        verify(navigationManager, times(0)).navigate(
            GeneralDestinations.MainScreenDestinations
        )

        loginViewModel.navigateToHomeScreen("exampleEmail@.com")

        verify(navigationManager, times(1)).navigate(
            GeneralDestinations.MainScreenDestinations,
            mapOf(
                "userId" to "exampleEmail@.com"
            )
        )
    }

    @Test
    fun navigateToRegisterScreen() {

        verify(navigationManager, times(0)).navigate(
            GeneralDestinations.RegisterScreen
        )

        loginViewModel.navigateToRegisterScreen()

        verify(navigationManager, times(1)).navigate(
            GeneralDestinations.RegistrationDestinations,
        )
    }

    @Test
    fun `sign in manually Successfully  `() = runTest {
        loginViewModel.state.test {
            awaitItem()

            userDaoFake.insertUser(
                UserEntity("Sam", "", lastName = "", password = "1232")
            )

            loginViewModel.attemptSignIn("Sam", "1232")

            val loginViewModelState = awaitItem()

            assertThat(loginViewModelState).isInstanceOf<LoginScreenStates.UserSignedInState>()

        }
    }
}