package com.example.googlelightcalendar.core.profile_screen

import androidx.lifecycle.ViewModel
import com.example.googlelightcalendar.navigation.components.MainScreenNavManager
import com.example.googlelightcalendar.navigation.components.NavigationDestinations
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.internal.synchronized
import javax.inject.Inject
@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val navigationManger: MainScreenNavManager,
) : ViewModel() {


    fun onBackPress() {
        navigationManger.onBackPress()
    }

    @OptIn(InternalCoroutinesApi::class)
    fun navigate(
        destinations: NavigationDestinations
    ) {
        synchronized(destinations){
            navigationManger.navigate(
                navigation = destinations,
                arguments = mapOf(
                    "userId" to "23"
                )
            )
        }
    }
}