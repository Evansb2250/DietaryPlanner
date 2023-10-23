package com.example.googlelightcalendar

import android.app.Application
import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore

class GoogleLightCalendarApplication: Application() {
    val Context.dataStore: DataStore<Preferences> by preferencesDataStore(GoogleToken)

    override fun onCreate() {
        super.onCreate()
    }
}