package com.example.googlelightcalendar.repo

import android.content.Intent
import androidx.activity.result.ActivityResultLauncher
import com.example.googlelightcalendar.auth.OauthClientImp
import com.example.googlelightcalendar.core.TokenManager
import com.example.googlelightcalendar.data.room.database.dao.UserDao
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
            signedIn: Boolean,
            serverResponse: String,
        ) -> Unit = { _, _ ->

        },
    )
}

@Singleton
class UserRepositoryImpl @Inject constructor(
    private val googleOauthClient: Lazy<OauthClientImp>,
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
            signedIn: Boolean,
            serverResponse: String,
        ) -> Unit
    ) {
        val asyncResponse = googleOauthClient.value.handleAuthorizationResponse(
            intent = intent,
            authorizationResponse = authorizationResponse,
            error = error,
        )
        withContext(Dispatchers.IO){
            when (asyncResponse) {
                is AsyncResponse.Failed<User?> -> {
                    authorizationResponseCallback(
                        false,
                        asyncResponse.message ?: "Failed"
                    )
                }

                is AsyncResponse.Success<User?> -> {

                    val user = userDao.getUserFromGmailSignIn(asyncResponse.data?.name ?: "")?.toUser()

                    if (user != null) {
                        // ALlo SignIn
//                        authorizationResponseCallback(
//                            false,
//                            "You need to register this account"
//                        )
                    } else {
                        // Have user register account.
                        authorizationResponseCallback(
                            false,
                            "You need to register this account"
                        )
                    }
                }
            }
        }
    }
}