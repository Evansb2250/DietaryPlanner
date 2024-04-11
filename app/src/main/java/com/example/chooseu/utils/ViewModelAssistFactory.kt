package com.example.chooseu.utils

import com.example.chooseu.ui.screens.calendar_access.CalendarSettingViewModel
import com.example.chooseu.ui.screens.home.HomeViewModel
import dagger.Module
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
class ViewModelAssistFactory {

    @AssistedFactory
    interface  HomeViewModelFactory{
        fun create(userId: String): HomeViewModel
    }

    @AssistedFactory
    interface  CalendarSettingFactory{
        fun create(userId: String): CalendarSettingViewModel
    }

}