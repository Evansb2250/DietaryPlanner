package com.example.googlelightcalendar.core.profile_screen

import androidx.lifecycle.ViewModel
import com.example.googlelightcalendar.R
import com.example.googlelightcalendar.navigation.components.NavigationDestinations
import com.example.googlelightcalendar.navigation.components.ProfileRoutes
import com.example.googlelightcalendar.navigation.components.navmanagers.AuthNavManager
import com.example.googlelightcalendar.navigation.components.navmanagers.BottomNavManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.internal.synchronized
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val bottomNavManager: BottomNavManager,
    private val authNavManager: AuthNavManager,
) : ViewModel() {
    val state = MutableStateFlow(ProfileScreenStates.ProfilePage).asStateFlow()

    fun logout(){
        authNavManager.navigate(
            NavigationDestinations.OnAppStartUp
        )
    }
    @OptIn(InternalCoroutinesApi::class)
    fun navigate(
        destinations: NavigationDestinations
    ) {
        synchronized(destinations){
            bottomNavManager.navigate(
                navigation = destinations,
                arguments = mapOf(
                    "userId" to "23"
                )
            )
        }
    }
}


sealed class ProfileScreenStates {
    object ProfilePage : ProfileScreenStates(){

        data class ProfileOptions(
            val leadingIconId: Int,
            val text: String,
            val trailingIconId: Int,
            val destination: NavigationDestinations,
        )


        val items = listOf(
            ProfileOptions(
                leadingIconId = R.drawable.profile_icon_2,
                text = "Account",
                trailingIconId = R.drawable.right_arrow,
                destination = ProfileRoutes.Account
            ),

            ProfileOptions(
                leadingIconId = R.drawable.notification_icon_2,
                text = "Notification",
                trailingIconId = R.drawable.right_arrow,
                destination = ProfileRoutes.Notifications
            ),

            ProfileOptions(
                leadingIconId = R.drawable.calendar_icon_2,
                text = "Calendar",
                trailingIconId = R.drawable.right_arrow,
                destination = ProfileRoutes.Calendar,
            ),

            ProfileOptions(
                leadingIconId = R.drawable.lock_icon,
                text = "Terms of Service",
                trailingIconId = R.drawable.right_arrow,
                destination = ProfileRoutes.TOS,
            )
        )
    }
}