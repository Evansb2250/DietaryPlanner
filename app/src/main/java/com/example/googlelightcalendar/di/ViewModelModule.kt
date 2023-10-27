package com.example.googlelightcalendar.di

import com.example.googlelightcalendar.GoogleOauthClient
import com.example.googlelightcalendar.TokenManager
import com.example.googlelightcalendar.repo.UserRepository
import com.example.googlelightcalendar.viewmodels.LoginViewModel
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
    ): LoginViewModel {
        return LoginViewModel(
            userRepository
        )
    }
}