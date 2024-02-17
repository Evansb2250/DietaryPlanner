package com.example.chooseu.core.on_startup

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.example.chooseu.core.dispatcher_provider.DispatcherProvider
import com.example.chooseu.core.on_startup.state.LastSignInState
import com.example.chooseu.data.rest.api_service.service.account.AccountService
import kotlinx.coroutines.withContext
import javax.inject.Inject

class OnAppStartUpManager @Inject constructor(
    private val accountService: AccountService,
    private val dispatcher: DispatcherProvider,
) {

    private var _finishedLoading = mutableStateOf(false)
    val isfinishedLoading = _finishedLoading

    var lastLoginState: LastSignInState by mutableStateOf(LastSignInState.NotLoggedIn)
        private set

    suspend fun fetchSignState() {
            withContext(dispatcher.io) {
                val user = accountService.getLoggedIn()

                if (user != null) {
                    lastLoginState = LastSignInState.AlreadyLoggedIn(user.id)
                } else {
                    lastLoginState = LastSignInState.NotLoggedIn
                }
            }
            _finishedLoading.value  = true
    }
}
