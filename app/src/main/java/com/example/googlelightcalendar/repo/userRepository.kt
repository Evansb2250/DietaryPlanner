package com.example.googlelightcalendar.repo

import android.content.Intent
import androidx.activity.result.ActivityResultLauncher
import com.example.googlelightcalendar.auth.OauthClientImp
import com.example.googlelightcalendar.core.GoogleTokenManagerImpl
import com.example.googlelightcalendar.core.TokenManager
import com.example.googlelightcalendar.data.room.database.dao.UserDao
import com.example.googlelightcalendar.data.room.database.models.toUser
import com.example.googlelightcalendar.domain.User
import com.example.googlelightcalendar.utils.AsyncResponse
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

    fun handleAuthorizationResponse(
        intent: Intent,
        authorizationResponse: (
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

        return when(result){
         null ->{
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

    override fun handleAuthorizationResponse(
        intent: Intent,
        authorizationResponse: (
            signedIn: Boolean,
            serverResponse: String,
        ) -> Unit
    ) {
        googleOauthClient.value.handleAuthorizationResponse(
            intent = intent,
            signInState = { asyncResponse ->
                when (asyncResponse) {
                    is AsyncResponse.Failed<*> -> {
                        authorizationResponse(
                            false,
                            asyncResponse.message ?:"Failed"
                        )
                    }

                    is AsyncResponse.Success<*> -> {
                        authorizationResponse(
                            true,
                            "SignedIn"
                        )
                    }
                }
            },
        )
    }
}