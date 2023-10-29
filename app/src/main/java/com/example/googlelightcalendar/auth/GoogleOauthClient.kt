package com.example.googlelightcalendar.auth

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.util.Base64
import android.util.Log
import androidx.activity.result.ActivityResultLauncher
import com.auth0.android.jwt.JWT
import com.example.googlelightcalendar.common.Constants
import com.example.googlelightcalendar.core.TokenManager
import com.example.googlelightcalendar.utils.AsyncResponse
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import net.openid.appauth.AuthState
import net.openid.appauth.AuthorizationException
import net.openid.appauth.AuthorizationRequest
import net.openid.appauth.AuthorizationRequest.Scope
import net.openid.appauth.AuthorizationResponse
import net.openid.appauth.ResponseTypeValues
import java.security.MessageDigest
import java.security.SecureRandom
import javax.inject.Inject
import javax.inject.Singleton


const val TAG = "GoogleOauthClient"

@Singleton
class GoogleOauthClient @Inject constructor(
    @ApplicationContext private val context: Context,
    private val oauthState: AuthorizationState,
    private val tokenManager: TokenManager,
    private val coroutineScope: CoroutineScope,
) {

    private var scopes = mutableListOf<String>()
    private var jwt: JWT? = null
    private lateinit var authorizationLauncher: ActivityResultLauncher<Intent>

    fun registerAuthLauncher(
        launcher: ActivityResultLauncher<Intent>,
    ) {
        // Perform any necessary setup or configuration of the launcher here
        // Then call the callback function when needed, e.g., after launching an activityb
        authorizationLauncher = launcher
    }


    fun attemptAuthorization(
        authorizationScopes: Array<String>
    ) {
        val secureRandom = SecureRandom()
        val bytes = ByteArray(64)
        secureRandom.nextBytes(bytes)

        val encoding: Int = Base64.URL_SAFE or Base64.NO_PADDING or Base64.NO_WRAP
        val codeVerifier: String = Base64.encodeToString(bytes, encoding)
        scopes.addAll(authorizationScopes)


        createCodeChallenge(
            codeVerifier,
            encoding
        )
    }

    fun createCodeChallenge(
        codeVerifier: String,
        encoding: Int
    ) {
        val digest = MessageDigest.getInstance(Constants.MESSAGE_DIGEST_ALGORITHM)
        val hash = digest.digest(codeVerifier.toByteArray())
        val codeChallenge: String = Base64.encodeToString(hash, encoding)

        baseauthRequestBuilder(
            codeVerifier,
            codeChallenge,
        )
    }

    fun baseauthRequestBuilder(
        codeVerifier: String,
        codeChallenge: String,
    ) {
        val builder = AuthorizationRequest.Builder(
            oauthState.getAuthServiceConfig(),
            Constants.CLIENT_ID,
            ResponseTypeValues.CODE,
            Uri.parse(Constants.URL_AUTH_REDIRECT)
        ).setCodeVerifier(
            codeVerifier,
            codeChallenge,
            Constants.CODE_VERIFIER_CHALLENGE_METHOD
        )
        builder.setScopes(scopes)

        val request = builder.build()
        val authIntent: Intent = oauthState.getAuthorizationRequestIntent(request)

        authorizationLauncher.launch(authIntent)
    }


    fun handleAuthorizationResponse(
        intent: Intent,
        signInState: (
            AsyncResponse<String>
        ) -> Unit = { _ -> }
    ) {
        val authorizationResponse: AuthorizationResponse? = AuthorizationResponse.fromIntent(intent)
        val error = AuthorizationException.fromIntent(intent)

        oauthState.updateAuthState(AuthState(authorizationResponse, error))

        val tokenExchangeRequest = authorizationResponse?.createTokenExchangeRequest()

        if (tokenExchangeRequest != null) {

            oauthState.performTokenRequest(tokenExchangeRequest) { response ->
                if (response != null) {
                    jwt = JWT(response.idToken!!)
                    Log.e(TAG, "Token received ${response.accessToken}")

                    saveToken(response.accessToken ?: "")

                    signInState(
                        AsyncResponse.Success(null)
                    )
                }
                persistState()
            }
        } else {
            signInState(
                AsyncResponse.Failed<String>(null, "couldn't get token")
            )
        }
    }

    private fun saveToken(
        authToken: String,
    ) {
        Log.d("GOOGLE AUTH", " Saving token $authToken")
        coroutineScope.launch {
            tokenManager.saveToken(authToken)
        }
    }

    fun persistState() {
        context.getSharedPreferences(Constants.SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE)
            .edit()
            .putString(Constants.AUTH_STATE, oauthState.toJsonSerializeString())
            .commit()
    }
}

