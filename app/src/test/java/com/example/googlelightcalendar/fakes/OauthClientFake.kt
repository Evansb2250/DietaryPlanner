package com.example.googlelightcalendar.fakes

import android.content.Intent
import androidx.activity.result.ActivityResultLauncher
import com.example.googlelightcalendar.auth.OauthClient

//Only testing to make sure the state of our OAuthClientFake is changed.
data class OAuthClientFake(
    var attemptToAuthorize: Boolean = false,
    var isRegistered: Boolean = false,
): OauthClient {

    override fun registerAuthLauncher(launcher: ActivityResultLauncher<Intent>) {
        isRegistered = true
    }

    override fun attemptAuthorization(authorizationScopes: Array<String>) {
        // can only be called if the launcher has been registered.
        if(isRegistered){
            attemptToAuthorize = true
        }
    }
}
