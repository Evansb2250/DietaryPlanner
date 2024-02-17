package com.example.chooseu.core.on_startup

import androidx.lifecycle.MutableLiveData
import com.example.chooseu.core.on_startup.state.LastSignInState
import com.example.chooseu.data.rest.api_service.service.account.AccountService

class OnAppStartUpManager(
    private val accountService: AccountService,
) {

    private var _isLoadingData = MutableLiveData<Boolean>(true)
    val isLoadingData = _isLoadingData
    var lastLoginState: LastSignInState = LastSignInState.NotLoggedIn
        private set

    suspend fun fetchSignState() {
        val user = accountService.getLoggedIn()

        if (user != null) {
            lastLoginState = LastSignInState.AlreadyLoggedIn(user.id)
        } else {
            lastLoginState = LastSignInState.NotLoggedIn
        }
        isLoadingData.postValue(false)
    }
}
