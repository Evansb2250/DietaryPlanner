package com.example.googlelightcalendar.auth

import android.content.Intent
import net.openid.appauth.AuthState
import net.openid.appauth.AuthorizationRequest
import net.openid.appauth.AuthorizationServiceConfiguration
import net.openid.appauth.TokenRequest
import net.openid.appauth.TokenResponse

interface AuthorizationState {
    fun getAuthorizationRequestIntent(request: AuthorizationRequest): Intent
    fun getAuthServiceConfig(): AuthorizationServiceConfiguration

    fun updateAuthState(authStateUpdate: AuthState)
    fun performTokenRequest(
        tokenRequest: TokenRequest?,
        response: (
            token: TokenResponse?,
        ) -> Unit,
    )
    fun toJsonSerializeString(): String
}
