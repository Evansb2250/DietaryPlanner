package com.example.chooseu.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import com.example.chooseu.auth.AuthorizationState
import com.example.chooseu.auth.OAuthStateHandler
import com.example.chooseu.auth.OauthClientImp
import com.example.chooseu.core.GoogleTokenManagerImpl
import com.example.chooseu.core.TokenManager
import com.example.chooseu.core.diary.cache.FoodItemCache
import com.example.chooseu.core.registration.cache.UserRegistrationCache
import com.example.chooseu.core.registration.cache.UserRegistrationCacheImpl
import com.example.chooseu.navigation.components.navmanagers.AuthNavManager
import com.example.chooseu.navigation.components.navmanagers.AppNavManager
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
        dataStore: DataStore<Preferences>
    ): TokenManager {
        return GoogleTokenManagerImpl(
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
        tokenManager: GoogleTokenManagerImpl,
        authState: OAuthStateHandler,
    ): Lazy<OauthClientImp> {
        return lazy {
            OauthClientImp(
                context = context,
                tokenManager = tokenManager,
                oauthState = authState,
            )
        }
    }

    @Provides
    @Singleton
    fun providesAuthNavigationManager(
        externalScope: CoroutineScope
    ): AuthNavManager = AuthNavManager(
        externalScope
    )

    @Provides
    @Singleton
    fun providesMainScreenNavigationManager(
        authNavManager: AuthNavManager,
        externalScope: CoroutineScope
    ): AppNavManager = AppNavManager(
        authNavManager,
        externalScope
    )

    @Provides
    @Singleton
    fun providesFoodItemCache(): FoodItemCache = FoodItemCache()

    @Provides
    @Singleton
    fun provideRegistrationCache(): UserRegistrationCache = UserRegistrationCacheImpl()
}