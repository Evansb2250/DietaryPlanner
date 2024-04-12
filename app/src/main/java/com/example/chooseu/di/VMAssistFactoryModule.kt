package com.example.chooseu.di

import com.example.chooseu.core.main_screen.BottomNavViewModel
import com.example.chooseu.core.profile_screen.ProfileViewModel
import com.example.chooseu.ui.screens.calendar_access.CalendarSettingViewModel
import com.example.chooseu.ui.screens.home.HomeViewModel
import dagger.Module
import dagger.assisted.AssistedFactory
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
class VMAssistFactoryModule {
    @AssistedFactory
    interface  BottomNavVmFactory {
        fun create(userId: String): BottomNavViewModel
    }

    @AssistedFactory
    interface  ProfileViewModelFactory {
        fun create(userId: String): ProfileViewModel
    }

    @AssistedFactory
    interface  CalendarSettingFactory{
        fun create(userId: String): CalendarSettingViewModel
    }

    @AssistedFactory
    interface  HomeViewModelFactory{
        fun create(userId: String): HomeViewModel
    }

}