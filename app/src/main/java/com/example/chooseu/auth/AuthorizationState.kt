package com.example.chooseu.auth

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
    suspend fun performTokenRequest(
        tokenRequest: TokenRequest?,
    ) : TokenResponse?
    fun toJsonSerializeString(): String

    fun getAuthorizationScopes(): Set<String>
}
