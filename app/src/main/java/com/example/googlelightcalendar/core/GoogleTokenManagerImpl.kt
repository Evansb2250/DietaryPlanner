package com.example.googlelightcalendar.core

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.example.googlelightcalendar.auth.Token
import com.example.googlelightcalendar.auth.createPreferenceKey
import com.example.googlelightcalendar.utils.TokenUtil
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

const val GoogleToken = "GoogleToken"

interface TokenManager {
    suspend fun saveToken(key: Token, token: String)
    suspend fun <T : Token> fetchToken(
        key: T
    ): Flow<String?>
}

@Singleton
class GoogleTokenManagerImpl @Inject constructor(
    private val dataStore: DataStore<Preferences>
) : TokenManager {
    val googleIdToken: Flow<String> = dataStore.data.map { preferences ->
        preferences[Token.GoogleToken.IdToken.createPreferenceKey()] ?: ""
    }

    val googleAccessToken: Flow<String> = dataStore.data.map { preferences ->
        preferences[Token.GoogleToken.AccessToken.createPreferenceKey()] ?: ""
    }

    val googleExpirationToken: Flow<String> = dataStore.data.map { preferences ->
        preferences[Token.GoogleToken.ExpirationToken.createPreferenceKey()] ?: ""
    }

    override suspend fun saveToken(key: Token, token: String) {
        val dataStoreKey =  TokenUtil.getTokenKey(key)

        val preferencesKey = stringPreferencesKey(dataStoreKey)

        dataStore.edit { pref ->
            pref[preferencesKey] = token
        }
    }
    override suspend fun <T : Token> fetchToken(token: T): Flow<String?> {
        return when (token) {
            is Token.GoogleToken.IdToken -> googleIdToken
            is Token.GoogleToken.AccessToken -> googleAccessToken
            is Token.GoogleToken.ExpirationToken -> googleExpirationToken
            else -> throw IllegalArgumentException("Invalid token type")
        }
    }
}

