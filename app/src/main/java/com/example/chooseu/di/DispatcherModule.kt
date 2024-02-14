package com.example.chooseu.di

import com.example.chooseu.core.dispatcherProvider.DispatcherProvider
import com.example.chooseu.core.dispatcherProvider.DispatcherProviderImp
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DispatcherModule {

    @Singleton
    @Provides
    fun providesDispatchProvider(): DispatcherProvider = DispatcherProviderImp()

}