package com.example.chooseu.ui.screens.onAppStartUpScreen

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import com.example.chooseu.common.DataStoreKeys
import com.example.chooseu.core.dispatcher_provider.DispatcherProvider
import com.example.chooseu.ui.screens.onAppStartUpScreen.state.LastSignInState
import com.example.chooseu.utils.TokenUtil
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
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

    suspend fun setOnAppStartUpState() {
        val pref = dataStore.data?.firstOrNull()
        val experiationDate = pref?.get(DataStoreKeys.USER_SESSION_EXPIRATION)
        val storedUserKey = pref?.get(DataStoreKeys.USER_ID)
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