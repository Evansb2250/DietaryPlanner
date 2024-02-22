package com.example.chooseu.repo

import android.content.Intent
import androidx.activity.result.ActivityResultLauncher
import assertk.assertThat
import assertk.assertions.isEqualTo
import com.example.chooseu.auth.OauthClient
import com.example.chooseu.auth.OauthClientImp
import com.example.chooseu.core.GoogleTokenManagerImpl
import com.example.chooseu.core.TokenManager
import com.example.chooseu.data.database.dao.UserDao
import com.example.chooseu.data.database.models.UserEntity
import com.example.chooseu.domain.CurrentUser
import com.example.chooseu.fakes.OAuthClientFake
import com.example.chooseu.fakes.TokenManagerFakeImpl
import com.example.chooseu.fakes.UserDaoFake
import com.example.chooseu.utils.AsyncResponse
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

        assertThat(result).isEqualTo(AsyncResponse.Failed<CurrentUser?>(null, "Incorrect credentials"))
    }


    @Test
    fun signInTestPassed() = runTest {
        userDao.insertUser(
            UserEntity("block@example", "dsa", "123", password = "")
        )
        val result: AsyncResponse<CurrentUser?> = repository.signIn("block@example", "123")

        assertThat(result).isEqualTo(AsyncResponse.Success<CurrentUser?>(data = CurrentUser("block@example", "dsa") ))
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