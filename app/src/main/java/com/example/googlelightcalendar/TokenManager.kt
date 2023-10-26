package com.example.googlelightcalendar

import android.app.Application
import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

const val GoogleToken = "GoogleToken"

@Singleton
class TokenManager @Inject constructor (
    private val context: Context,
    private val dataStore: DataStore<Preferences>
){
    val googleToken: Flow<String> =  dataStore.data.map { preferences ->
        preferences[GOOGLE_TOKEN] ?: ""
    }
    suspend fun saveToken(token: String) {
        dataStore.edit { pref ->
            pref[GOOGLE_TOKEN] = token
        }
    }

    suspend fun fetchToken(): Flow<String?> = googleToken

    companion object{
        private val GOOGLE_TOKEN : Preferences.Key<String> = stringPreferencesKey("GoogleToken")
     }

}