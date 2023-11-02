package com.example.googlelightcalendar.auth

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.util.Log
import com.example.googlelightcalendar.common.Constants
import kotlinx.coroutines.suspendCancellableCoroutine
import net.openid.appauth.AppAuthConfiguration
import net.openid.appauth.AuthState
import net.openid.appauth.AuthorizationException
import net.openid.appauth.AuthorizationRequest
import net.openid.appauth.AuthorizationService
import net.openid.appauth.AuthorizationServiceConfiguration
import net.openid.appauth.TokenRequest
import net.openid.appauth.TokenResponse
import net.openid.appauth.browser.BrowserAllowList
import net.openid.appauth.browser.VersionedBrowserMatcher
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.coroutines.resume

@Singleton
class OAuthStateHandler @Inject constructor(
    private val context: Context,
) : AuthorizationState {

    private var authState: AuthState = AuthState()
    private lateinit var authorizationService: AuthorizationService
    private lateinit var authServiceConfig: AuthorizationServiceConfiguration

    init {
        initAuthService()
        initAuthServiceConfig()
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

    override fun getAuthorizationRequestIntent(request: AuthorizationRequest): Intent {
        return authorizationService.getAuthorizationRequestIntent(request)
    }

    override fun getAuthServiceConfig(): AuthorizationServiceConfiguration {
        return authServiceConfig
    }

    override fun updateAuthState(authStateUpdate: AuthState) {
        authState = authStateUpdate
    }

    suspend override fun performTokenRequest(tokenRequest: TokenRequest?): TokenResponse? {
        return suspendCancellableCoroutine { continuation ->
            // retrieves the token request if it isn't null we will use this token to get an access Token
            if (tokenRequest != null) {

                authorizationService.performTokenRequest(tokenRequest) { response, exception ->
                    try {
                        if (exception != null) {
                            //we update our authState an exception was found
                            authState = AuthState()
                        } else {
                            if (response != null) {
                                authState.update(response, exception)
                                continuation.resume(response)
                            }
                        }
                    } catch (e: AuthorizationException) {
                        continuation.resume(null)
                    }
                }
            } else {
                continuation.resume(null)
            }
        }
    }

    override fun toJsonSerializeString(): String {
        return authState.jsonSerializeString()
    }

    override fun getAuthorizationScopes(): Set<String> {
        return authState.scopeSet?.mapNotNull { it }?.toSet() ?: emptySet()
    }

//    fun signOutWithoutRedirect(
//        signInState: (Boolean) -> Unit = {}
//    ) {
//        GlobalScope.launch {
//            val client = OkHttpClient()
//            val request = Request.Builder()
//                .url(Constants.URL_LOGOUT + authState.idToken)
//                .build()
//
//            try {
//                client.newCall(request).execute()
//            } catch (e: IOException) {
//            } finally {
//                signInState(false)
//            }
//        }
//    }

}


//    suspend fun getCalendar(): String {
//  //      val token: String = tokenManager.googleToken.firstOrNull() ?: "Nothing inside"
//   //     Log.d("CalendarRepo", "Auth $token")
//
//        val response = try {
//    //        GoogleCalendarClient.getService().getCalendar("Bearer $token").await()
//        } catch (e: Exception) {
//            Log.e("CalendarRepo", "Error making API call: ${e.message}")
//            null
//        }
//
//        if (response != null) {
//            Log.d("CalendarRepo", "Response: $response")
//            return response
//        } else {
//            Log.d("CalendarRepo", "API call failed, returning default value")
//            return "Default Response"
//        }
//    }
