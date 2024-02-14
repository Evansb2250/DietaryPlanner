package com.example.chooseu.di

import android.content.Context
import com.example.chooseu.core.main_screen.BottomNavViewModel
import com.example.chooseu.core.profile_screen.ProfileViewModel
import com.example.chooseu.core.registration.RegisterGoalViewModel
import com.example.chooseu.core.registration.UserRegistrationCache
import com.example.chooseu.core.viewmodels.login.LoginViewModel
import com.example.chooseu.navigation.components.navmanagers.AuthNavManager
import com.example.chooseu.navigation.components.navmanagers.AppNavManager
import com.example.chooseu.repo.UserRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.qualifiers.ApplicationContext

@Module
@InstallIn(ViewModelComponent::class)
object ViewModelModule {
    @Provides
    fun providesLoginViewModel(
        userRepository: UserRepository,
        navigationManger: AuthNavManager,
        @ApplicationContext context: Context,
    ): LoginViewModel {
        return LoginViewModel(
            navigationManager = navigationManger,
            userRepository = userRepository,
            context = context,
        )
    }

    @Provides
    fun providesRegisterGoalViewModel(
        navigationManger: AuthNavManager,
        userRegistrationCache: UserRegistrationCache,
        userRepository: UserRepository,
    ): RegisterGoalViewModel = RegisterGoalViewModel(
        navManager = navigationManger,
        userRegistrationCache = userRegistrationCache,
        userRepository = userRepository,
    )

    @Provides
    fun providesProfileViewModel(
        bottomNavManager: AppNavManager,
        authNavManager: AuthNavManager,
    ): ProfileViewModel = ProfileViewModel(
        bottomNavManager = bottomNavManager,
        authNavManager = authNavManager,
    )

    @Provides
    fun providesBottomNavViewModel(
        navigationManger: AppNavManager
    ): BottomNavViewModel = BottomNavViewModel(
        navigationManager = navigationManger,
    )
}