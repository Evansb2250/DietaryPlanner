package com.example.googlelightcalendar

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

const val GoogleToken = "GoogleToken"
class TokenManager (
    private val context: Context,
){
    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(GoogleToken)

    val googleToken: Flow<String> =  context.dataStore.data.map { preferences ->
        preferences[GOOGLE_TOKEN] ?: ""
    }
    suspend fun saveToken(token: String) {
        context.dataStore.edit { pref ->
            pref[GOOGLE_TOKEN] = token
        }
    }

    suspend fun fetchToken(): Flow<String?> = googleToken

    companion object{
        private val GOOGLE_TOKEN : Preferences.Key<String> = stringPreferencesKey("GoogleToken")
     }

}