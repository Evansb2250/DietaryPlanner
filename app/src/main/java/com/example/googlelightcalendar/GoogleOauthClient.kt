package com.example.googlelightcalendar

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.util.Base64
import android.util.Log
import androidx.activity.result.ActivityResultLauncher
import com.auth0.android.jwt.JWT
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import net.openid.appauth.AppAuthConfiguration
import net.openid.appauth.AuthState
import net.openid.appauth.AuthorizationException
import net.openid.appauth.AuthorizationRequest
import net.openid.appauth.AuthorizationResponse
import net.openid.appauth.AuthorizationService
import net.openid.appauth.AuthorizationServiceConfiguration
import net.openid.appauth.ResponseTypeValues
import net.openid.appauth.browser.BrowserAllowList
import net.openid.appauth.browser.VersionedBrowserMatcher
import okhttp3.OkHttpClient
import okhttp3.Request
import org.json.JSONObject
import java.io.IOException
import java.security.MessageDigest
import java.security.SecureRandom
import javax.inject.Inject
import javax.inject.Singleton


const val TAG = "GoogleOauthClient"

@Singleton
class GoogleOauthClient @Inject constructor(
    @ApplicationContext private val context: Context,
    private val tokenManager: TokenManager,
    private val coroutineScope: CoroutineScope
) {

    private var authState: AuthState = AuthState()
    private var jwt: JWT? = null
    private lateinit var authorizationService: AuthorizationService
    private lateinit var authServiceConfig: AuthorizationServiceConfiguration
    private lateinit var authorizationLauncher: ActivityResultLauncher<Intent>

    init {
        initAuthService()
        initAuthServiceConfig()
    }


    fun registerAuthLauncher(
        launcher: ActivityResultLauncher<Intent>,
    ) {
        // Perform any necessary setup or configuration of the launcher here
        // Then call the callback function when needed, e.g., after launching an activityb
        authorizationLauncher = launcher
    }


    private fun initAuthService() {
        val appAuthConfiguration = AppAuthConfiguration.Builder()
            .setBrowserMatcher(
                BrowserAllowList(
                    VersionedBrowserMatcher.CHROME_CUSTOM_TAB,
                    VersionedBrowserMatcher.SAMSUNG_CUSTOM_TAB
                )
            ).build()

        authorizationService = AuthorizationService(
            context.applicationContext,
            appAuthConfiguration
        )
    }


    private fun initAuthServiceConfig() {
        authServiceConfig = AuthorizationServiceConfiguration(
            Uri.parse(Constants.URL_AUTHORIZATION),
            Uri.parse(Constants.URL_TOKEN_EXCHANGE),
            null,
            Uri.parse(Constants.URL_LOGOUT)
        )
    }

    fun attemptAuthorization() {
        val secureRandom = SecureRandom()
        val bytes = ByteArray(64)
        secureRandom.nextBytes(bytes)

        val encoding = Base64.URL_SAFE or Base64.NO_PADDING or Base64.NO_WRAP
        val codeVerifier = Base64.encodeToString(bytes, encoding)

        createCodeChallenge(
            codeVerifier,
            encoding
        )

    }

    fun handleAuthorizationResponse(
        intent: Intent,
        signInState: (
            Boolean,
            String,
        ) -> Unit = {_, _ -> }
    ) {
        val authorizationResponse: AuthorizationResponse? = AuthorizationResponse.fromIntent(intent)
        val error = AuthorizationException.fromIntent(intent)

        authState = AuthState(authorizationResponse, error)

        val tokenExchangeRequest = authorizationResponse?.createTokenExchangeRequest()

        if (tokenExchangeRequest != null) {
            authorizationService.performTokenRequest(tokenExchangeRequest) { response, exception ->
                if (exception != null) {
                    authState = AuthState()
                } else {
                    if (response != null) {
                        authState.update(response, exception)
                        Log.d(TAG, "Response  ${authState.jsonSerializeString()}")
//                        response.accessToken
//                        response.refreshToken
//                        response.accessTokenExpirationTime
//

                        jwt = JWT(response.idToken!!)
                        Log.e(TAG, "Token received ${response.accessToken}")

                        saveToken(response.accessToken ?: "")

                        signInState(true, "SignedInt")
                    }
                }
                persistState()
            }
        } else {
            signInState(false, "Failed Sign In")
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
            .putString(Constants.AUTH_STATE, authState.jsonSerializeString())
            .commit()
    }

    fun signOutWithoutRedirect(
        signInState: (Boolean) -> Unit = {}
    ) {
        GlobalScope.launch {
            val client = OkHttpClient()
            val request = Request.Builder()
                .url(Constants.URL_LOGOUT + authState.idToken)
                .build()

            try {
                client.newCall(request).execute()
            } catch (e: IOException) {
            } finally {
                signInState(false)
            }

        }
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
        codeChallenge: String
    ) {
        val builder = AuthorizationRequest.Builder(
            authServiceConfig,
            Constants.CLIENT_ID,
            ResponseTypeValues.CODE,
            Uri.parse(Constants.URL_AUTH_REDIRECT)
        ).setCodeVerifier(
            codeVerifier,
            codeChallenge,
            Constants.CODE_VERIFIER_CHALLENGE_METHOD
        )
        builder.setScopes(
            Constants.SCOPE_PROFILE,
            Constants.SCOPE_EMAIL,
            Constants.SCOPE_OPENID,
            Constants.CALENDAR_SCOPE,
            Constants.CALENDAR_EVENTS,
            Constants.CALENDAR_READ_ONLY,
        )


        val request = builder.build()
        val authIntent: Intent = authorizationService.getAuthorizationRequestIntent(request)


        authorizationLauncher.launch(authIntent)
    }

    fun makeApiCall() {
        authState.performActionWithFreshTokens(
            authorizationService,
            object : AuthState.AuthStateAction {
                override fun execute(
                    accessToken: String?,
                    idToken: String?,
                    ex: AuthorizationException?
                ) {
                    GlobalScope.launch {
                        async(Dispatchers.IO) {
                            val client = OkHttpClient()
                            val request = Request.Builder()
                                .url(Constants.URL_API_CALL)
                                .addHeader("Authorization", "Bearer " + authState.accessToken)
                                .build()

                            try {
                                val response = client.newCall(request).execute()
                                val jsonBody = response.body?.string() ?: ""
                                Log.i(TAG, JSONObject(jsonBody).toString())
                            } catch (e: Exception) {
                            }
                        }
                    }
                }
            },
        )
    }

}

