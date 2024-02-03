package com.example.googlelightcalendar.core.profile_screen

import androidx.lifecycle.ViewModel
import com.example.googlelightcalendar.R
import com.example.googlelightcalendar.navigation.components.MainScreenNavManager
import com.example.googlelightcalendar.navigation.components.NavigationDestinations
import com.example.googlelightcalendar.navigation.components.ProfileRoutes
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.internal.synchronized
import javax.inject.Inject
@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val navigationManger: MainScreenNavManager,
) : ViewModel() {
    val state = MutableStateFlow(ProfileScreenStates.ProfilePage).asStateFlow()

    fun logout(){
        navigationManger.logout()
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