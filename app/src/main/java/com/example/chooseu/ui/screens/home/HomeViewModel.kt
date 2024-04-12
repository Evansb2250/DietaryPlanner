package com.example.chooseu.ui.screens.home

import androidx.lifecycle.ViewModel
import com.example.chooseu.di.VMAssistFactoryModule
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel

@HiltViewModel(
    assistedFactory = VMAssistFactoryModule.HomeViewModelFactory::class,
)
class HomeViewModel @AssistedInject constructor(
    @Assisted userId: String,
) : ViewModel(){

}