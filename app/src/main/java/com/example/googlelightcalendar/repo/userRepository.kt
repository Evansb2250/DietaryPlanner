package com.example.googlelightcalendar.repo

import android.content.Intent
import androidx.activity.result.ActivityResultLauncher
import com.example.googlelightcalendar.auth.OauthClient
import com.example.googlelightcalendar.core.TokenManager
import com.example.googlelightcalendar.data.room.database.dao.UserDao
import com.example.googlelightcalendar.data.room.database.models.UserEntity
import com.example.googlelightcalendar.data.room.database.models.toUser
import com.example.googlelightcalendar.domain.User
import com.example.googlelightcalendar.utils.AsyncResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import net.openid.appauth.AuthorizationException
import net.openid.appauth.AuthorizationResponse
import javax.inject.Inject
import javax.inject.Singleton

interface UserRepository {
    fun attemptAuthorization(
        authorizationScopes: Array<String>
    )

    fun registerAuthLauncher(
        launcher: ActivityResultLauncher<Intent>
    )

    suspend fun signIn(
        userName: String,
        password: String,
    ): AsyncResponse<User?>

    suspend fun handleAuthorizationResponse(
        intent: Intent,
        authorizationResponse: AuthorizationResponse? = AuthorizationResponse.fromIntent(intent),
        error: AuthorizationException? = AuthorizationException.fromIntent(intent),
        authorizationResponseCallback: (
            AuthorizationResponseStates,
        ) -> Unit = {},
    )

     suspend fun handleSignUpResponse(
        intent: Intent,
        authorizationResponse: AuthorizationResponse?,
        error: AuthorizationException?,
        authorizationResponseCallback: (
            AuthorizationResponseStates
        ) -> Unit
    )

     suspend fun createUser(
         email: String,
         firstName: String,
         lastName: String,
         password: String,
     )
}

sealed class AuthorizationResponseStates {
    data class SuccessResponseState(
        val email: String,
        val name: String,
    ) : AuthorizationResponseStates()

    data class FirstTimeUserState(
        val email: String,
        val name: String = "",
    ) : AuthorizationResponseStates()

    data class FailedResponsState(
        val message: String,
    ) : AuthorizationResponseStates()
}

@Singleton
class UserRepositoryImpl @Inject constructor(
    private val googleOauthClient: Lazy<OauthClient>,
    private val userDao: UserDao,
    private val tokenManager: TokenManager,
) : UserRepository {

    // Aysnc response is received in the ActivityResultLauncher in the loginScreen
    override fun attemptAuthorization(
        authorizationScopes: Array<String>
    ) {
        googleOauthClient.value.attemptAuthorization(authorizationScopes)
    }

    //Registers the googleOauthClient to the activity launcher the googleOauthClient is a singleton, and it survives while mainActivity is alive.
    override fun registerAuthLauncher(launcher: ActivityResultLauncher<Intent>) {
        googleOauthClient.value.registerAuthLauncher(launcher)
    }

    override suspend fun signIn(userName: String, password: String): AsyncResponse<User?> {
        val result = userDao.getUser(
            userName,
            password
        )

        return when (result) {
            null -> {
                AsyncResponse.Failed(
                    data = null,
                    message = "Incorrect credentials"
                )
            }

            else -> {
                AsyncResponse.Success(
                    data = result.toUser(),
                )
            }
        }
    }

    override suspend fun handleAuthorizationResponse(
        intent: Intent,
        authorizationResponse: AuthorizationResponse?,
        error: AuthorizationException?,
        authorizationResponseCallback: (
            AuthorizationResponseStates
        ) -> Unit
    ) {
        val asyncResponse = googleOauthClient.value.handleAuthorizationResponse(
            intent = intent,
            authorizationResponse = authorizationResponse,
            error = error,
        )

        withContext(Dispatchers.IO) {
            when (asyncResponse) {
                is AsyncResponse.Failed<User?> -> {
                    authorizationResponseCallback(
                        AuthorizationResponseStates.FailedResponsState(
                            asyncResponse.message ?: "Failed"
                        )
                    )
                }

                is AsyncResponse.Success<User?> -> {
                    val user = userDao.getUserFromGmailSignIn(asyncResponse.data?.name ?: "")?.toUser()

                    if (user != null) {
                        authorizationResponseCallback(
                            AuthorizationResponseStates.SuccessResponseState(
                                email = user.userName,
                                name = user.name,
                            )
                        )
                    } else {
                        // Have user register account.
                        authorizationResponseCallback(
                            AuthorizationResponseStates.FirstTimeUserState(
                                asyncResponse.data!!.userName,
                                asyncResponse.data.name,
                                )
                        )
                    }
                }
            }
        }
    }

    override suspend fun handleSignUpResponse(
        intent: Intent,
        authorizationResponse: AuthorizationResponse?,
        error: AuthorizationException?,
        authorizationResponseCallback: (AuthorizationResponseStates) -> Unit
    ) {
        TODO("Not yet implemented")
    }

    override suspend fun createUser(
        email: String,
        firstName: String,
        lastName: String,
        password: String
    ) {
        userDao.insertUser(
            UserEntity(
                userName = email,
                name = firstName,
                lastName = lastName,
                password = password
            )
        )
    }
}