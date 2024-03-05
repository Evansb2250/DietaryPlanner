package com.example.chooseu.utils

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import com.example.chooseu.common.DataStoreKeys.USER_BIRTH_DATE
import com.example.chooseu.common.DataStoreKeys.USER_DOC_ID
import com.example.chooseu.common.DataStoreKeys.USER_EMAIL
import com.example.chooseu.common.DataStoreKeys.USER_FIRST_NAME
import com.example.chooseu.common.DataStoreKeys.USER_GENDER
import com.example.chooseu.common.DataStoreKeys.USER_HEIGHT
import com.example.chooseu.common.DataStoreKeys.USER_HEIGHT_METRIC
import com.example.chooseu.common.DataStoreKeys.USER_ID
import com.example.chooseu.common.DataStoreKeys.USER_LAST_NAME
import com.example.chooseu.common.DataStoreKeys.USER_SESSION_EXPIRATION
import com.example.chooseu.common.DataStoreKeys.USER_WEIGHT
import com.example.chooseu.common.DataStoreKeys.USER_WEIGHT_METRIC
import io.appwrite.models.Document


object DataStoreUtil {
    suspend fun DataStore<Preferences>.storeUserData(
        userData: Document<Map<String, Any>>,
    ): AsyncResponse<Unit> {
        return try {
            this.edit { preferences ->
                // Map preferences to CurrentUser
                preferences[USER_DOC_ID] = userData.id
                preferences[USER_ID] = userData.data[USER_ID.name] as String
                preferences[USER_FIRST_NAME] = userData.data[USER_FIRST_NAME.name] as String
                preferences[USER_LAST_NAME] = userData.data[USER_LAST_NAME.name] as String
                preferences[USER_GENDER] = userData.data[USER_GENDER.name] as String
                preferences[USER_EMAIL] = userData.data[USER_EMAIL.name] as String
                preferences[USER_BIRTH_DATE] = userData.data[USER_BIRTH_DATE.name] as String
            }
            AsyncResponse.Success(data = null)
        } catch (e: ClassCastException) {
            AsyncResponse.Failed(data = null, message = e.message)
        } catch (e: Exception) {
            AsyncResponse.Failed(data = null, message = e.message)
        }
    }


    suspend fun DataStore<Preferences>.clearUserData() {
        try {
            this.edit { preferences ->
                // Map preferences to CurrentUser
                preferences[USER_DOC_ID] = ""
                preferences[USER_ID] = ""
                preferences[USER_SESSION_EXPIRATION] = ""
                preferences[USER_FIRST_NAME] = ""
                preferences[USER_LAST_NAME] = ""
                preferences[USER_GENDER] = ""
                preferences[USER_EMAIL] = ""
                preferences[USER_BIRTH_DATE] = ""
                preferences[USER_HEIGHT_METRIC] = ""
                preferences[USER_HEIGHT] = 0.0
                preferences[USER_WEIGHT_METRIC] = ""
                preferences[USER_WEIGHT] = 0.0
            }
        } catch (e: Exception) {

        }
    }
}

