package com.example.googlelightcalendar.repo

import android.content.Intent
import androidx.activity.result.ActivityResultLauncher
import assertk.assertThat
import assertk.assertions.isEqualTo
import com.example.googlelightcalendar.auth.OauthClient
import com.example.googlelightcalendar.auth.OauthClientImp
import com.example.googlelightcalendar.core.GoogleTokenManagerImpl
import com.example.googlelightcalendar.core.TokenManager
import com.example.googlelightcalendar.data.database.dao.UserDao
import com.example.googlelightcalendar.data.database.models.UserEntity
import com.example.googlelightcalendar.domain.User
import com.example.googlelightcalendar.fakes.OAuthClientFake
import com.example.googlelightcalendar.fakes.TokenManagerFakeImpl
import com.example.googlelightcalendar.fakes.UserDaoFake
import com.example.googlelightcalendar.utils.AsyncResponse
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.internal.verification.NoInteractions
import org.mockito.internal.verification.Times
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify

class UserRepositoryImplTest {
    private lateinit var repository: UserRepository
    private lateinit var googleOauthClient: OauthClient
    private lateinit var userDao: UserDao
    private lateinit var tokenManager: TokenManager


    @BeforeEach
    fun setUp(){

        googleOauthClient = OAuthClientFake()
        userDao = UserDaoFake()
        tokenManager = TokenManagerFakeImpl()

        repository = UserRepositoryImpl(
            googleOauthClient = lazy { googleOauthClient },
            userDao = userDao,
            tokenManager = tokenManager,
        )
    }

    @Test
    fun attemptAuthorizationTest(){
        val googleOauthClient: OauthClientImp = mock()
        val userUserDao: UserDao = mock()
        val tokenManager: GoogleTokenManagerImpl = mock()
        val emptyArray = emptyArray<String>()

        val repositoryMock = UserRepositoryImpl(
            lazy { googleOauthClient },
            userUserDao,
            tokenManager
        )

        repositoryMock.attemptAuthorization(emptyArray)

        verify(
            googleOauthClient,
            Times(1),
            ).attemptAuthorization(emptyArray)
    }


    @Test
    fun registerAuthLauncher() {
        val intent : ActivityResultLauncher<Intent> = mock()
        val googleOauthClient: OauthClientImp = mock()
        val userUserDao: UserDao = mock()
        val tokenManager: GoogleTokenManagerImpl = mock()

        val repositoryMock = UserRepositoryImpl(
            lazy { googleOauthClient },
            userUserDao,
            tokenManager
        )

        verify(
            googleOauthClient,
            NoInteractions(),
        ).registerAuthLauncher(intent)

        repositoryMock.registerAuthLauncher(intent)


        verify(
            googleOauthClient,
            Times(1)
        ).registerAuthLauncher(intent)
    }

    @Test
    fun signInTest() = runTest {
        userDao.insertUser(
            UserEntity("dsads", "dsa", "sadsa", password = "")
        )
        val result = repository.signIn("dsa", "ds")

        assertThat(result).isEqualTo(AsyncResponse.Failed<User?>(null, "Incorrect credentials"))
    }


    @Test
    fun signInTestPassed() = runTest {
        userDao.insertUser(
            UserEntity("block@example", "dsa", "123", password = "")
        )
        val result: AsyncResponse<User?> = repository.signIn("block@example", "123")

        assertThat(result).isEqualTo(AsyncResponse.Success<User?>(data = User("block@example", "dsa") ))
    }

    @Test
    fun handleAuthorizationResponseTest() = runTest{
        val intent = mock<Intent>()

        //TODO("must mock the server response")
        // Add a stateFlow
        repository.handleAuthorizationResponse(
            intent = intent,
        )
    }


}