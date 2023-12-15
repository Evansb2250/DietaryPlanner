package com.example.googlelightcalendar.fakes

import android.content.ActivityNotFoundException
import android.content.Intent
import androidx.activity.result.ActivityResultLauncher
import com.example.googlelightcalendar.auth.OauthClient
import com.example.googlelightcalendar.domain.User
import com.example.googlelightcalendar.utils.AsyncResponse
import net.openid.appauth.AuthorizationException
import net.openid.appauth.AuthorizationResponse

//Only testing to make sure the state of our OAuthClientFake is changed.
data class OAuthClientFake(
    var attemptToAuthorize: Boolean = false,
    var isRegistered: Boolean = false,
): OauthClient {
    var emailAddress: String? = null
    override fun registerAuthLauncher(launcher: ActivityResultLauncher<Intent>) {
        isRegistered = true
    }

    override fun attemptAuthorization(authorizationScopes: Array<String>) {
        // can only be called if the launcher has been registered.
        if(isRegistered){
            attemptToAuthorize = true
        } else {
            throw ActivityNotFoundException("Launcher is not registered")
        }
    }

    override suspend fun handleAuthorizationResponse(
        intent: Intent,
        authorizationResponse: AuthorizationResponse?,
        error: AuthorizationException?
    ): AsyncResponse<User?> {
        return if(this.emailAddress == null){
            AsyncResponse.Failed(data = null, message = "failed Oauth process")
        }else {
            AsyncResponse.Success(User(this.emailAddress ?: "default", ""))
        }
    }

    fun setGmailAccount(
        emailAddress: String?
    ){
        this.emailAddress = emailAddress
    }

    fun cancelOauthProcess(){
        attemptToAuthorize = false
    }
}
