package com.example.chooseu.core.on_startup

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import com.example.chooseu.core.dispatcher_provider.DispatcherProvider
import com.example.chooseu.core.on_startup.state.LastSignInState
import com.example.chooseu.utils.TokenUtil
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class OnAppStartUpManager @Inject constructor(
    private val dataStore: DataStore<Preferences>,
    private val dispatcher: DispatcherProvider,
) {

    private var _finishedLoading = mutableStateOf(false)
    val isfinishedLoading = _finishedLoading

    var lastLoginState: LastSignInState by mutableStateOf(LastSignInState.NotLoggedIn)
        private set

    suspend fun getPreferencesFlow(): Flow<Preferences> = dataStore.data

    fun setOnAppStartUpState(storedUserKey: String?, experiationDate: String?) {
        setLoginStateOnStartUp(experiationDate, storedUserKey)
        _finishedLoading.value = true
    }


    fun setLoginStateOnStartUp(
        experiationDate: String?,
        storedUserKey: String?,
    ) {
        if (
            !experiationDate.isNullOrEmpty() &&
            !storedUserKey.isNullOrEmpty() &&
            !TokenUtil.isTokenExpired(experiationDate)
        ) {
            lastLoginState = LastSignInState.AlreadyLoggedIn(storedUserKey)
        } else {
            lastLoginState = LastSignInState.NotLoggedIn
        }
    }

}