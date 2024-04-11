package com.example.chooseu.utils

import com.example.chooseu.ui.screens.calendar_access.CalendarSettingViewModel
import dagger.Module
import dagger.assisted.AssistedFactory
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
class ViewModelAssistFactory {

    @AssistedFactory
    interface  CalendarSettingFactory{

        fun create(userId: String): CalendarSettingViewModel
    }

}