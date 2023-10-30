package com.example.googlelightcalendar.fakes

import android.content.Intent
import androidx.activity.result.ActivityResultLauncher
import com.example.googlelightcalendar.auth.OauthClient
import com.example.googlelightcalendar.data.room.database.dao.UserDao
import com.example.googlelightcalendar.data.room.database.models.toUser
import com.example.googlelightcalendar.domain.User
import com.example.googlelightcalendar.repo.UserRepository
import com.example.googlelightcalendar.utils.AsyncResponse

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
                data = null,
                message = "user not found"
            )

            else -> AsyncResponse.Success(
                data = result.toUser()
            )
        }
    }

    override fun handleAuthorizationResponse(
        intent: Intent,
        authorizationResponse: (signedIn: Boolean, serverResponse: String) -> Unit
    ) {
      authorizationResponse(
          true,
          "Will Mock this later"
      )
    }
}