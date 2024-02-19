package com.example.chooseu.repo

import android.content.Intent
import androidx.activity.result.ActivityResultLauncher
import com.example.chooseu.core.registration.state.RegisterGoalStates
import com.example.chooseu.domain.User
import com.example.chooseu.utils.AsyncResponse
import net.openid.appauth.AuthorizationException
import net.openid.appauth.AuthorizationResponse

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
        userInfo: Map<String, String>,
    ): AsyncResponse<RegisterGoalStates>
}