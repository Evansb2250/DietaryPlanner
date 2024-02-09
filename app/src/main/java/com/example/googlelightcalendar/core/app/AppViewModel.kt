package com.example.googlelightcalendar.core.app

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.googlelightcalendar.navigation.components.AuthNavManager
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class AppViewModel @Inject constructor(
    val navigationManager: AuthNavManager,
) : ViewModel() {

    var showExitAppNotification by mutableStateOf(false)
        private set


    fun closeExitDialog(){
        showExitAppNotification = false
    }

    fun showExitDialog(){
        showExitAppNotification = true
    }

}