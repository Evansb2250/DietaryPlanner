package com.example.chooseu.di

import android.content.Context
import com.example.chooseu.data.database.GoogleLightCalenderDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RoomModule {
    @Provides
    @Singleton
    fun provideGoogleCalendarDatabase(
        @ApplicationContext context: Context,
    ): GoogleLightCalenderDatabase {
        return GoogleLightCalenderDatabase.getDatabase(context)
    }
}