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
import net.openid.appauth.AuthorizationResponse
import net.openid.appauth.ResponseTypeValues
import java.security.MessageDigest
import java.security.SecureRandom
import javax.inject.Inject
import javax.inject.Singleton


const val TAG = "GoogleOauthClient"

interface OauthClient {
    var authorizationLauncher: ActivityResultLauncher<Intent>

    fun registerAuthLauncher(
        launcher: ActivityResultLauncher<Intent>,
    )

    fun attemptAuthorization(
        authorizationScopes: Array<String>
    )
}


@Singleton
class OauthClientImp @Inject constructor(
    @ApplicationContext private val context: Context,
    private val oauthState: AuthorizationState,
    private val tokenManager: TokenManager,
    private val coroutineScope: CoroutineScope,
) : OauthClient {

    private var scopes = mutableListOf<String>()
    private var jwt: JWT? = null
    override lateinit var authorizationLauncher: ActivityResultLauncher<Intent>

    override fun registerAuthLauncher(
        launcher: ActivityResultLauncher<Intent>,
    ) {
        // Perform any necessary setup or configuration of the launcher here
        // Then call the callback function when needed, e.g., after launching an activityb
        authorizationLauncher = launcher
    }


    override fun attemptAuthorization(
        authorizationScopes: Array<String>
    ) {
        //clear previous scopes
        scopes.clear()

        val secureRandom = SecureRandom()
        val bytes = ByteArray(64)
        secureRandom.nextBytes(bytes)

        val encoding: Int = Base64.URL_SAFE or Base64.NO_PADDING or Base64.NO_WRAP
        val codeVerifier: String = Base64.encodeToString(bytes, encoding)

        //add new scopes
        scopes.addAll(authorizationScopes)


        createCodeChallenge(
            codeVerifier,
            encoding
        )
    }

    private fun createCodeChallenge(
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

    private fun baseauthRequestBuilder(
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
        try {
            val authorizationResponse: AuthorizationResponse? =
                AuthorizationResponse.fromIntent(intent)
            val error: AuthorizationException? = AuthorizationException.fromIntent(intent)


            oauthState.updateAuthState(AuthState(authorizationResponse, error))

            val tokenExchangeRequest = authorizationResponse?.createTokenExchangeRequest()

            if (tokenExchangeRequest != null) {
                oauthState.performTokenRequest(tokenExchangeRequest) { response ->
                    if (response != null) {
                        jwt = JWT(response.idToken!!)

                        saveToken( TokenType.Access, response.accessToken ?: "")
                        saveToken(TokenType.ID, response.idToken ?: "")

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
        } catch (e: Exception) {
            signInState(
                AsyncResponse.Failed<String>(null, "Error found")
            )
        }
    }

    private fun saveToken(
        tokenType: TokenType,
        authToken: String,
    ) {
        Log.d("GOOGLE AUTH", " Saving token $authToken")
        coroutineScope.launch {
            tokenManager.saveToken(
                tokenManager.getTokenType(tokenType), authToken
            )
        }
    }

    private fun persistState() {
        context.getSharedPreferences(Constants.SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE)
            .edit()
            .putString(Constants.AUTH_STATE, oauthState.toJsonSerializeString())
            .commit()
    }
}

