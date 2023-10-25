package com.example.googlelightcalendar.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import com.example.googlelightcalendar.GoogleOauthClient
import com.example.googlelightcalendar.TokenManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineScope
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class AppModule {
    @Provides
    @Singleton
    fun providesTokenManager(
        @ApplicationContext context: Context,
        dataStore: DataStore<Preferences>
    ): TokenManager {
        return TokenManager(
            context,
            dataStore
        )
    }

    @Provides
    @Singleton
    fun providesGoogleAuthClient(
        @ApplicationContext context: Context,
        tokenManager: TokenManager,
        coroutineScope: CoroutineScope,
    ): Lazy<GoogleOauthClient> {
        return lazy {
            GoogleOauthClient(
                context = context,
                tokenManager = tokenManager,
                coroutineScope = coroutineScope
            )
        }
    }

}