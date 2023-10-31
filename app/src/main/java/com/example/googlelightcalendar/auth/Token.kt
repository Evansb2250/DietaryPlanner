package com.example.googlelightcalendar.auth

import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.stringPreferencesKey

sealed class Token(
    open val tokenType: String
) {
    sealed class GoogleToken(
        open val tokenName: String
    ) : Token(
        tokenType = tokenName
    ) {
        object IdToken : GoogleToken("GoogleToken")
        object AccessToken : GoogleToken("AccessToken")
        object ExpirationToken : GoogleToken("ExpirationToken")
    }

}


fun Token.createPreferenceKey():  Preferences.Key<String> = stringPreferencesKey(this.tokenType)