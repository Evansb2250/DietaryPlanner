package com.example.chooseu.common

import androidx.datastore.preferences.core.doublePreferencesKey
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey

object DataStoreKeys {
    val USER_SESSION_EXPIRATION = stringPreferencesKey("expiration")
    val USER_ID = stringPreferencesKey("userId")
    val USER_FIRST_NAME = stringPreferencesKey("firstName")
    val USER_LAST_NAME = stringPreferencesKey("lastName")
    val USER_GENDER = stringPreferencesKey("gender")
    val USER_EMAIL = stringPreferencesKey("email")
    val USER_BIRTH_DATE = stringPreferencesKey("birthDate")
    val USER_HEIGHT_METRIC = stringPreferencesKey("heightMetric")
    val USER_HEIGHT = doublePreferencesKey("height")
    val USER_WEIGHT_METRIC = stringPreferencesKey("weightMetric")
    val USER_WEIGHT = doublePreferencesKey("weight")
    val USER_DOC_ID = stringPreferencesKey("user_doc_id")


    //For BMI
    val BMI_STORED_DATE = intPreferencesKey("dateInteger")
    val BMI_VALUE = doublePreferencesKey("bmi")

}