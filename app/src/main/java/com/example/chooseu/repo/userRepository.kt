package com.example.chooseu.repo

import com.example.chooseu.domain.BodyMassIndex
import com.example.chooseu.domain.CurrentUser
import com.example.chooseu.utils.AsyncResponse
import kotlinx.coroutines.flow.Flow

interface UserRepository {

    val currentUser: Flow<CurrentUser?>
    suspend fun signIn(
        userName: String,
        password: String,
    ): AsyncResponse<Unit>

    suspend fun clearPrefsOnSignOut()

    suspend fun getBMIHistory(): List<BodyMassIndex>
    suspend fun createUserInServer(
        userInfo: Map<String, String>,
    ): UpdateResult

    suspend fun addNewBodyMassIndexToServer(
        weight: Double,
        weightMetric: String,
        height: Double,
        heightMetric: String,
    ): UpdateResult
}