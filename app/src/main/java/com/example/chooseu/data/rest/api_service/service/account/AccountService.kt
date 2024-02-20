package com.example.chooseu.data.rest.api_service.service.account

import com.example.chooseu.utils.AsyncResponse
import io.appwrite.Client
import io.appwrite.ID
import io.appwrite.models.User
import io.appwrite.exceptions.AppwriteException
import io.appwrite.services.Account

class AccountService(client: Client) {
    private val account = Account(client)

    suspend fun getLoggedIn(): AsyncResponse<User<Map<String, Any>>?> {
        return try {
            AsyncResponse.Success(
                data = account.get()
            )
        } catch (e: AppwriteException) {
            AsyncResponse.Failed(
                data = null,
                message = e.message,
            )
        }
    }

    suspend fun login(email: String, password: String): AsyncResponse<User<Map<String, Any>>?> {
        return try {
            account.createEmailSession(email, password)
            getLoggedIn()
        } catch (e: AppwriteException) {
            AsyncResponse.Failed(
                data = null,
                message = e.message,
            )
        }
    }

    suspend fun register(
        userId: String,
        email: String,
        password: String,
        name: String
    ): AsyncResponse<Unit> {
        return try {
            account.create(
                userId = userId,
                email = email,
                password = password,
                name = name,
            )

            AsyncResponse.Success(
                data = Unit
            )
        } catch (e: AppwriteException) {
            AsyncResponse.Failed(
                data = null,
                message = e.message,
            )
        }
    }

    suspend fun logout() {
        account.deleteSession("current")
    }
}
