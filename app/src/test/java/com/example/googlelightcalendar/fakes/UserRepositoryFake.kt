package com.example.googlelightcalendar.fakes

import android.content.Intent
import androidx.activity.result.ActivityResultLauncher
import com.example.googlelightcalendar.auth.OauthClient
import com.example.googlelightcalendar.data.room.database.dao.UserDao
import com.example.googlelightcalendar.data.room.database.models.toUser
import com.example.googlelightcalendar.domain.User
import com.example.googlelightcalendar.repo.AuthorizationResponseStates
import com.example.googlelightcalendar.repo.UserRepository
import com.example.googlelightcalendar.utils.AsyncResponse
import net.openid.appauth.AuthorizationException
import net.openid.appauth.AuthorizationResponse

class UserRepositoryFake(
    val oauthClient: OauthClient,
    val userDaoFake: UserDao,
) : UserRepository {
    override fun attemptAuthorization(authorizationScopes: Array<String>) {
        oauthClient.attemptAuthorization(authorizationScopes)
    }

    override fun registerAuthLauncher(launcher: ActivityResultLauncher<Intent>) {
        oauthClient.registerAuthLauncher(launcher)
    }

    override suspend fun signIn(userName: String, password: String): AsyncResponse<User?> {
        val result = userDaoFake.getUser(userName, password)

        return when (result) {
            null -> AsyncResponse.Failed(
                data = null, message = "user not found"
            )

            else -> AsyncResponse.Success(
                data = result.toUser()
            )
        }
    }

    override suspend fun handleAuthorizationResponse(
        intent: Intent,
        authorizationResponse: AuthorizationResponse?,
        error: AuthorizationException?,
        authorizationResponseCallback: (AuthorizationResponseStates) -> Unit
    ) {

        if ((oauthClient as OAuthClientFake).isRegistered && (oauthClient as OAuthClientFake).attemptToAuthorize) {

            val userExist = userDaoFake.getUserFromGmailSignIn(oauthClient.emailAddress ?: "Unkown")

            if (userExist != null) {
                authorizationResponseCallback(
                    AuthorizationResponseStates.SuccessResponseState(
                        oauthClient.emailAddress!!, "Will Mock this later"
                    )
                )
            } else {
                authorizationResponseCallback(
                    AuthorizationResponseStates.FirstTimeUserState(
                        ""
                    )
                )
            }

        } else {
            authorizationResponseCallback(
                AuthorizationResponseStates.FailedResponsState(
                    ""
                )
            )
        }

    }
}