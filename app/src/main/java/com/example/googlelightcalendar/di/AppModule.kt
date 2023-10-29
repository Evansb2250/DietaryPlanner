package com.example.googlelightcalendar.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import com.example.googlelightcalendar.auth.AuthorizationState
import com.example.googlelightcalendar.auth.OAuthStateHandler
import com.example.googlelightcalendar.auth.GoogleOauthClient
import com.example.googlelightcalendar.core.TokenManager
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
    fun providesGoogleAuthState(
        @ApplicationContext context: Context,
    ): AuthorizationState {
        return OAuthStateHandler(context)
    }

    @Provides
    @Singleton
    fun providesGoogleAuthClient(
        @ApplicationContext context: Context,
        tokenManager: TokenManager,
        coroutineScope: CoroutineScope,
        authState: OAuthStateHandler,
    ): Lazy<GoogleOauthClient> {
        return lazy {
            GoogleOauthClient(
                context = context,
                tokenManager = tokenManager,
                coroutineScope = coroutineScope,
                oauthState = authState,
            )
        }
    }

}