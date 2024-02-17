package com.example.chooseu.di

import android.content.Context
import com.example.chooseu.common.Constants
import com.example.chooseu.data.rest.api_service.service.account.AccountService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import io.appwrite.Client
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppWriteModule {
    @Singleton
    @Provides
    fun provideAppWriteClient(
        @ApplicationContext context: Context,
    ): Client = Client(
        context = context
    ).setEndpoint(Constants.appwriteEndPoint)
        .setProject(Constants.appWriteProjectId)


    @Singleton
    @Provides
    fun providesAccountService(
        appWriteClient: Client,
    ): AccountService = AccountService(appWriteClient)
}