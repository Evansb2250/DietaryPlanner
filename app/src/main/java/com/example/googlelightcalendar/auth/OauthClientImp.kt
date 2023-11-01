package com.example.googlelightcalendar.auth

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.util.Base64
import android.util.Log
import androidx.activity.result.ActivityResultLauncher
import com.auth0.android.jwt.JWT
import com.example.googlelightcalendar.common.Constants
import com.example.googlelightcalendar.core.TokenManager
import com.example.googlelightcalendar.domain.User
import com.example.googlelightcalendar.utils.AsyncResponse
import com.example.googlelightcalendar.utils.TokenUtil.getTokenType
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier
import com.google.api.client.http.javanet.NetHttpTransport
import com.google.api.client.json.gson.GsonFactory
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import net.openid.appauth.AuthState
import net.openid.appauth.AuthorizationException
import net.openid.appauth.AuthorizationRequest
import net.openid.appauth.AuthorizationResponse
import net.openid.appauth.ResponseTypeValues
import net.openid.appauth.TokenResponse
import java.security.MessageDigest
import java.security.SecureRandom
import java.util.Collections
import javax.inject.Inject
import javax.inject.Singleton


const val TAG = "GoogleOauthClient"

interface OauthClient {


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
        } catch (e: ActivityNotFoundException) {
            // Create an error state here catches error.
        }
    }


    fun handleAuthorizationResponse(
        intent: Intent,
        authorizationResponse: AuthorizationResponse? = AuthorizationResponse.fromIntent(intent),
        error: AuthorizationException? = AuthorizationException.fromIntent(intent),
        authorizationResponseCallback: (
            AsyncResponse<User?>
        ) -> Unit = { _ -> },
    ) {
        try {

            val coroutineHandler = CoroutineExceptionHandler { _, exception ->
                //If we can't verify the GoogleToken throw an exception.
                println("CoroutineExceptionHandler got ${exception.message}")
                authorizationResponseCallback(
                    AsyncResponse.Failed(
                        data = null,
                        message = exception.message,
                    )
                )
            }

            oauthState.updateAuthState(AuthState(authorizationResponse, error))

            val tokenExchangeRequest = authorizationResponse?.createTokenExchangeRequest()

            if (tokenExchangeRequest != null) {
                oauthState.performTokenRequest(tokenExchangeRequest) { response ->
                    if (response != null) {
                        jwt = JWT(response.idToken!!)

                        storeTokenResponse(response)

                        validateGoogleToken(
                            tokenId = response.idToken ?: "",
                            coroutineScopeHandler = coroutineHandler,
                        ) { email, name ->

                            //If we can't attach the email to the account we should throw an error.
                            if (email.isNullOrBlank()) {
                                throw Exception(
                                    "Missing information to create a user.",
                                )
                            }

                            authorizationResponseCallback(
                                AsyncResponse.Success(
                                    data = User(
                                        userName = email,
                                        name = name ?: "",
                                    )
                                )
                            )
                        }
                    }
                    persistState()
                }
            } else {
                authorizationResponseCallback(
                    AsyncResponse.Failed(null, "couldn't get token")
                )
            }
        } catch (e: Exception) {
            authorizationResponseCallback(
                AsyncResponse.Failed(null, "Error found")
            )
        }
    }

    //Needs to be ran in a suspend function.
    fun validateGoogleToken(
        tokenId: String,
        coroutineScopeHandler: CoroutineExceptionHandler,
        userInfoCallBack: (String?, String?) -> Unit = { _, _ -> }
    ) {
        /**
         * Needs to be ran in a coroutine to move the work of the main thread.
         */
        coroutineScope.launch(coroutineScopeHandler) {
            val transport = NetHttpTransport()
            val json = GsonFactory.getDefaultInstance()
            val verifier: GoogleIdTokenVerifier = GoogleIdTokenVerifier.Builder(
                transport,
                json
            ).setAudience(
                Collections.singleton(Constants.CLIENT_ID)
            ).build()

            val idToken = verifier.verify(tokenId)
            if (idToken != null) {
                userInfoCallBack(
                    idToken.getPayload().email,
                    idToken.getPayload().get("name") as String,
                )
                Log.d(
                    "googleIdTokenValidation ", " email ${idToken.getPayload().email}"
                )
            } else
                throw Exception("Couldn't verify token")
        }
    }

    private fun storeTokenResponse(
        response: TokenResponse,
    ) {
        val accessToken: String = response.accessToken ?: return
        val idToken: String = response.idToken ?: return
        coroutineScope.launch {

            tokenManager.saveToken(
                getTokenType(TokenType.Access), accessToken,
            )

            tokenManager.saveToken(
                getTokenType(TokenType.ID), idToken,
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

