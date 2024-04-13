package com.example.chooseu.di

import com.example.chooseu.core.dispatcher_provider.DispatcherProvider
import com.example.chooseu.ui.screens.register.goal_creation.RegisterGoalViewModel
import com.example.chooseu.core.cache.UserRegistrationCache
import com.example.chooseu.navigation.components.navmanagers.AuthNavManager
import com.example.chooseu.repo.UserRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
object ViewModelModule {

    @Provides
    fun providesRegisterGoalViewModel(
        navigationManger: AuthNavManager,
        userRegistrationCache: UserRegistrationCache,
        userRepository: UserRepository,
        dispatcherProvider: DispatcherProvider,
    ): RegisterGoalViewModel = RegisterGoalViewModel(
        navManager = navigationManger,
        userRegistrationCache = userRegistrationCache,
        userRepository = userRepository,
        dispatcherProvider = dispatcherProvider,
    )

}