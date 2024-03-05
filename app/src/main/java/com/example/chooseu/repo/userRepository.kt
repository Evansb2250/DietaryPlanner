package com.example.chooseu.repo

import android.content.Intent
import androidx.activity.result.ActivityResultLauncher
import com.example.chooseu.data.database.models.BMIEntity
import com.example.chooseu.domain.CurrentUser
import com.example.chooseu.utils.AsyncResponse
import kotlinx.coroutines.flow.Flow
import net.openid.appauth.AuthorizationException
import net.openid.appauth.AuthorizationResponse

interface UserRepository {

    val currentUser: Flow<CurrentUser?>
    fun attemptAuthorization(
        authorizationScopes: Array<String>
    )

    fun registerAuthLauncher(
        launcher: ActivityResultLauncher<Intent>
    )

    suspend fun signIn(
        userName: String,
        password: String,
    ): AsyncResponse<Unit>

    suspend fun clearPrefsAndSignOut()

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

    suspend fun getBMIHistory(): List<BMIEntity>
    suspend fun createUserInServer(
        userInfo: Map<String, String>,
    ): UpdateResult

    suspend fun addNewBodyMassIndexToServer(
        weight: Double,
        weightMetric: String,
        height: Double,
        heightMetric: String,
    ): UpdateResult
}