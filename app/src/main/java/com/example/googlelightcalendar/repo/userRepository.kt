package com.example.googlelightcalendar.repo

import android.content.Intent
import androidx.activity.result.ActivityResultLauncher
import com.example.googlelightcalendar.GoogleOauthClient
import com.example.googlelightcalendar.TokenManager
import com.example.googlelightcalendar.data.room.UserDao
import dagger.hilt.android.scopes.ActivityScoped
import javax.inject.Inject
import javax.inject.Singleton

interface UserRepository {
    fun attemptAuthorization()

    fun registerAuthLauncher(
        launcher: ActivityResultLauncher<Intent>
    )

    suspend fun signIn(
        userName: String,
        password: String,
    )

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
    private val googleOauthClient: Lazy<GoogleOauthClient>,
    private val userDao : UserDao,
    private val tokenManager: TokenManager,
) : UserRepository {

    override fun attemptAuthorization() {
        googleOauthClient.value.attemptAuthorization()
    }

    override fun registerAuthLauncher(launcher: ActivityResultLauncher<Intent>) {
        googleOauthClient.value.registerAuthLauncher(launcher)
    }

    override suspend fun signIn(userName: String, password: String) {
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
            signInState = authorizationResponse,
        )
    }

}