package com.example.chooseu.di

import com.example.chooseu.auth.OauthClientImp
import com.example.chooseu.core.GoogleTokenManagerImpl
import com.example.chooseu.data.database.GoogleLightCalenderDatabase
import com.example.chooseu.repo.UserRepository
import com.example.chooseu.repo.UserRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.Dispatchers
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Singleton
    @Provides
    fun providesUserRepository(
        googleOauthClient: OauthClientImp,
        tokenManager: GoogleTokenManagerImpl,
        database: GoogleLightCalenderDatabase,
    ): UserRepository {
        return UserRepositoryImpl(
            googleOauthClient = lazy { googleOauthClient },
            userDao = database.userDao(),
            tokenManager = tokenManager,
            dispatcher = Dispatchers.IO,
        )
    }
}