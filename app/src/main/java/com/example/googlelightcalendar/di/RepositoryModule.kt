package com.example.googlelightcalendar.di

import com.example.googlelightcalendar.auth.GoogleOauthClient
import com.example.googlelightcalendar.core.TokenManager
import com.example.googlelightcalendar.data.room.database.GoogleLightCalenderDatabase
import com.example.googlelightcalendar.repo.UserRepository
import com.example.googlelightcalendar.repo.UserRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Singleton
    @Provides
    fun providesUserRepository(
        googleOauthClient: GoogleOauthClient,
        tokenManager: TokenManager,
        database: GoogleLightCalenderDatabase,
    ): UserRepository {
        return UserRepositoryImpl(
            googleOauthClient = lazy { googleOauthClient },
            userDao = database.userDao(),
            tokenManager = tokenManager
        )
    }
}