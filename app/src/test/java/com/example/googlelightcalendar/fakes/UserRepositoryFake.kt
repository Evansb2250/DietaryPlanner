package com.example.googlelightcalendar.fakes

import android.content.Intent
import androidx.activity.result.ActivityResultLauncher
import com.example.googlelightcalendar.data.room.database.dao.UserDao
import com.example.googlelightcalendar.data.room.database.models.toUser
import com.example.googlelightcalendar.domain.User
import com.example.googlelightcalendar.repo.UserRepository
import com.example.googlelightcalendar.utils.AsyncResponse

data class GoogleOAuthClientFake(
    var attemptToAuthorize: Boolean = false,
    var isRegistered: Boolean = false,
)


class UserRepositoryFake(
    val userDaoFake: UserDao
) : UserRepository {
    var authStateScopes = mutableListOf<String>()
    var googleOAuthClient = GoogleOAuthClientFake()
    override fun attemptAuthorization(authorizationScopes: Array<String>) {
        authStateScopes.addAll(authorizationScopes)
    }

    override fun registerAuthLauncher(launcher: ActivityResultLauncher<Intent>) {
        googleOAuthClient.isRegistered = true
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