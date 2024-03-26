package com.example.chooseu.di

import com.example.chooseu.data.rest.api_service.EdamamFoodApiClient
import com.example.chooseu.data.rest.api_service.EdamamFoodApiService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class EdamamApiModule {

    @Singleton
    @Provides
    fun providesFoodApiService(): EdamamFoodApiService = EdamamFoodApiClient.getService()
}