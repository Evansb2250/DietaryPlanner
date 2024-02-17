package com.example.chooseu.repo

import android.content.Intent
import androidx.activity.result.ActivityResultLauncher
import com.example.chooseu.auth.OauthClient
import com.example.chooseu.core.TokenManager
import com.example.chooseu.core.dispatcher_provider.DispatcherProvider
import com.example.chooseu.data.database.dao.UserDao
import com.example.chooseu.data.database.models.toUser
import com.example.chooseu.data.rest.api_service.service.account.AccountService
import com.example.chooseu.domain.User
import com.example.chooseu.utils.AsyncResponse
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.withContext
import net.openid.appauth.AuthorizationException
import net.openid.appauth.AuthorizationResponse
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.coroutines.resume

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

    suspend fun signOut()

    suspend fun handleAuthorizationResponse(
        intent: Intent,
        authorizationResponse: AuthorizationResponse? = AuthorizationResponse.fromIntent(intent),
        error: AuthorizationException? = AuthorizationException.fromIntent(intent),
    ): AuthorizationResponseStates

    suspend fun handleSignUpResponse(
        intent: Intent,
        authorizationResponse: AuthorizationResponse?,
        error: AuthorizationException?,
    ): AuthorizationResponseStates

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
    private val accountService: AccountService,
    private val tokenManager: TokenManager,
    private val dispatcherProvider: DispatcherProvider,
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

    override suspend fun signIn(userName: String, password: String): AsyncResponse<User?> =
        withContext(dispatcherProvider.io) {
            //
            accountService.login(email = userName, password)

            val result = accountService.getLoggedIn()

            return@withContext when (result) {
                null -> {
                    AsyncResponse.Failed(
                        data = null,
                        message = "Incorrect credentials"
                    )
                }

                else -> {
                    AsyncResponse.Success(
                        data = User(
                            result.email,
                            ""
                        ),
                    )
                }
            }
        }

    override suspend fun signOut() {
        withContext(dispatcherProvider.io) {
            accountService.logout()
        }
    }

    override suspend fun handleAuthorizationResponse(
        intent: Intent,
        authorizationResponse: AuthorizationResponse?,
        error: AuthorizationException?,
    ): AuthorizationResponseStates {
        return withContext(dispatcherProvider.io) {
            val asyncResponse = googleOauthClient.value.handleAuthorizationResponse(
                intent = intent,
                authorizationResponse = authorizationResponse,
                error = error,
            )
            suspendCancellableCoroutine { continuation ->
                when (asyncResponse) {
                    is AsyncResponse.Failed<User?> -> {
                        continuation.resume(
                            AuthorizationResponseStates.FailedResponsState(
                                asyncResponse.message ?: "Failed"
                            )
                        )
                    }

                    is AsyncResponse.Success<User?> -> {
                        val user =
                            userDao.getUserFromGmailSignIn(asyncResponse.data?.userName ?: "")
                                ?.toUser()

                        if (user != null) {
                            continuation.resume(
                                AuthorizationResponseStates.SuccessResponseState(
                                    email = user.userName,
                                    name = user.name,
                                )
                            )
                        } else {
                            // Have user register account.
                            continuation.resume(
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
    }

    override suspend fun handleSignUpResponse(
        intent: Intent,
        authorizationResponse: AuthorizationResponse?,
        error: AuthorizationException?
    ): AuthorizationResponseStates {
        TODO("Not yet implemented")
    }

    override suspend fun createUser(
        email: String,
        firstName: String,
        lastName: String,
        password: String
    ) {
        withContext(dispatcherProvider.io) {
            accountService.register(
                email = email,
                password = password,
                name = "$firstName $lastName"
            )
        }
    }
}