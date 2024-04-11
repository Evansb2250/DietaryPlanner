package com.example.chooseu.ui.screens.home

import androidx.lifecycle.ViewModel
import com.example.chooseu.utils.ViewModelAssistFactory
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel(
    assistedFactory = ViewModelAssistFactory.HomeViewModelFactory::class,
)
class HomeViewModel @AssistedInject constructor(
    @Assisted userId: String,
) : ViewModel(){

}