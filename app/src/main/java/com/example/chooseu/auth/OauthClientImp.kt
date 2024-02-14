package com.example.chooseu.auth

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.util.Base64
import androidx.activity.result.ActivityResultLauncher
import com.auth0.android.jwt.JWT
import com.example.chooseu.common.Constants
import com.example.chooseu.common.Constants.Companion.CLIENT_SECRET
import com.example.chooseu.core.TokenManager
import com.example.chooseu.domain.User
import com.example.chooseu.utils.AsyncResponse
import com.example.chooseu.utils.TokenUtil.getTokenType
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier
import com.google.api.client.http.javanet.NetHttpTransport
import com.google.api.client.json.gson.GsonFactory
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import net.openid.appauth.AuthState
import net.openid.appauth.AuthorizationException
import net.openid.appauth.AuthorizationRequest
import net.openid.appauth.AuthorizationResponse
import net.openid.appauth.ResponseTypeValues
import net.openid.appauth.TokenResponse
import java.security.GeneralSecurityException
import java.security.MessageDigest
import java.security.SecureRandom
import java.util.Collections
import javax.inject.Inject
import javax.inject.Singleton


interface OauthClient {

    fun registerAuthLauncher(
        launcher: ActivityResultLauncher<Intent>,
    )

    fun attemptAuthorization(
        authorizationScopes: Array<String>
    )

    suspend fun handleAuthorizationResponse(
        intent: Intent,
        authorizationResponse: AuthorizationResponse?,
        error: AuthorizationException?,
    ): AsyncResponse<User?>
}


@Singleton
class OauthClientImp @Inject constructor(
    @ApplicationContext private val context: Context,
    private val oauthState: AuthorizationState,
    private val tokenManager: TokenManager,
) : OauthClient {

    private var scopes = mutableListOf<String>()
    private var jwt: JWT? = null
    private lateinit var authorizationLauncher: ActivityResultLauncher<Intent>

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
        try {
            val builder = AuthorizationRequest.Builder(
                oauthState.getAuthServiceConfig(),
                CLIENT_SECRET,
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
        } catch (e: ActivityNotFoundException) {
            // Create an error state here catches error.
        }
    }

    suspend override fun handleAuthorizationResponse(
        intent: Intent,
        authorizationResponse: AuthorizationResponse?,
        error: AuthorizationException?
    ): AsyncResponse<User?> {

        oauthState.updateAuthState(AuthState(authorizationResponse, error))

        val tokenExchangeRequest = authorizationResponse?.createTokenExchangeRequest()

        if (tokenExchangeRequest != null) {
            val response = oauthState.performTokenRequest(tokenExchangeRequest)

            if (response != null) {
                storeTokenResponse(response)

                //TODO check if this withContext is needed
                val (googleEmail, name) = withContext(Dispatchers.IO) {
                    //This function needs to be ran off the main thread, so I am using withContext to
                    // return it back to IO thread after the operation is done.
                    validateGoogleToken(
                        tokenId = response.idToken ?: "",
                    )
                }

                if (googleEmail.isNullOrBlank()) {
                    throw Exception(
                        "Missing Email Information from Token"
                    )
                }

                return AsyncResponse.Success(
                    data = User(
                        userName = googleEmail,
                        name = name ?: ""
                    )
                )
            }

            return AsyncResponse.Failed(
                data = null,
                message = "response was null"
            )

        } else
            return AsyncResponse.Failed(
                data = null,
                message = "tokenExchangeRequest was null"
            )
    }

    //Needs to be ran off the main thread.
    fun validateGoogleToken(
        tokenId: String,
    ): Pair<String?, String?> {
        try{
            val transport = NetHttpTransport()
            val json = GsonFactory.getDefaultInstance()
            val verifier: GoogleIdTokenVerifier = GoogleIdTokenVerifier.Builder(
                transport,
                json
            ).setAudience(
                Collections.singleton(CLIENT_SECRET)
            ).build()

            val idToken = verifier.verify(tokenId)
            if (idToken != null) {

                return Pair(
                    idToken.getPayload().email,
                    idToken.getPayload().get("name") as String,
                )

            } else {
                return (Pair(null, null))
            }
        }catch (e: GeneralSecurityException){
            throw Exception("Can't validate Token")
        }
    }

    private suspend fun storeTokenResponse(
        response: TokenResponse,
    ) {
        val accessToken: String = response.accessToken ?: return
        val idToken: String = response.idToken ?: return

        tokenManager.saveToken(
            getTokenType(TokenType.Access), accessToken,
        )

        tokenManager.saveToken(
            getTokenType(TokenType.ID), idToken,
        )
    }
}

