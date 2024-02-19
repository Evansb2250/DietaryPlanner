package com.example.chooseu.di

import com.example.chooseu.auth.OauthClientImp
import com.example.chooseu.core.GoogleTokenManagerImpl
import com.example.chooseu.core.dispatcher_provider.DispatcherProvider
import com.example.chooseu.data.database.GoogleLightCalenderDatabase
import com.example.chooseu.data.rest.api_service.service.account.AccountService
import com.example.chooseu.data.rest.api_service.service.user.UserService
import com.example.chooseu.repo.UserRepository
import com.example.chooseu.repo.UserRepositoryImpl
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
        accountService: AccountService,
        googleOauthClient: OauthClientImp,
        tokenManager: GoogleTokenManagerImpl,
        database: GoogleLightCalenderDatabase,
        dispatcherProvider: DispatcherProvider,
        userService: UserService,
    ): UserRepository {
        return UserRepositoryImpl(
            googleOauthClient = lazy { googleOauthClient },
            userDao = database.userDao(),
            accountService = accountService,
            tokenManager = tokenManager,
            dispatcherProvider = dispatcherProvider,
            userService = userService,
        )
    }
}