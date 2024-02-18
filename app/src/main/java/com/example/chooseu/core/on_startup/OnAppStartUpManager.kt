package com.example.chooseu.core.on_startup

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.example.chooseu.core.dispatcher_provider.DispatcherProvider
import com.example.chooseu.core.on_startup.state.LastSignInState
import com.example.chooseu.data.rest.api_service.service.account.AccountService
import com.example.chooseu.utils.AsyncResponse
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
                val response = accountService.getLoggedIn()

                when(response){
                    is AsyncResponse.Failed -> {
                        lastLoginState = LastSignInState.NotLoggedIn
                    }
                    is AsyncResponse.Success -> {
                        lastLoginState = LastSignInState.AlreadyLoggedIn(response.data!!.id)
                    }
                }
            }
            _finishedLoading.value  = true
    }
}
