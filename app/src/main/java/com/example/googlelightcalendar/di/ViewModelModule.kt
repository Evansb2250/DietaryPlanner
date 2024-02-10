package com.example.googlelightcalendar.di

import com.example.googlelightcalendar.core.main_screen.BottomNavViewModel
import com.example.googlelightcalendar.core.profile_screen.ProfileViewModel
import com.example.googlelightcalendar.core.registration.RegisterGoalViewModel
import com.example.googlelightcalendar.core.registration.RegistrationViewModel
import com.example.googlelightcalendar.core.registration.UserRegistrationCache
import com.example.googlelightcalendar.core.viewmodels.login.LoginViewModel
import com.example.googlelightcalendar.navigation.components.navmanagers.AuthNavManager
import com.example.googlelightcalendar.navigation.components.navmanagers.BottomNavManager
import com.example.googlelightcalendar.repo.UserRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
object ViewModelModule {
    @Provides
    fun providesLoginViewModel(
        userRepository: UserRepository,
        navigationManger: AuthNavManager,
    ): LoginViewModel {
        return LoginViewModel(
            navigationManager = navigationManger,
            userRepository = userRepository,
        )
    }

    @Provides
    fun providesRegistrationViewMode(
        registrationCache: UserRegistrationCache,
        userRepository: UserRepository,
        navigationManger: AuthNavManager,
    ): RegistrationViewModel {
        return RegistrationViewModel(
            registrationCache = registrationCache,
            userRepository = userRepository,
            navigationManger = navigationManger,
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
        bottomNavManager: BottomNavManager,
        authNavManager: AuthNavManager,
    ): ProfileViewModel = ProfileViewModel(
        bottomNavManager = bottomNavManager,
        authNavManager = authNavManager,
    )

    @Provides
    fun providesBottomNavViewModel(
        navigationManger: BottomNavManager
    ): BottomNavViewModel = BottomNavViewModel(
        navigationManager = navigationManger,
    )
}