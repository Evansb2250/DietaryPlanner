package com.example.chooseu.data.rest.api_service.service.account

import com.example.chooseu.utils.AsyncResponse
import io.appwrite.Client
import io.appwrite.ID
import io.appwrite.models.User
import io.appwrite.exceptions.AppwriteException
import io.appwrite.models.Session
import io.appwrite.services.Account

class AccountService(client: Client) {
    private val account = Account(client)

    suspend fun getLoggedInUser(): AsyncResponse<User<Map<String, Any>>?> {
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

    suspend fun login(email: String, password: String): AsyncResponse<Session> {
        return try {
            AsyncResponse.Success(
                data = account.createEmailSession(email, password)
            )
        } catch (e: AppwriteException) {
            AsyncResponse.Failed(
                data = null,
                message = e.message,
            )
        }
    }

    suspend fun registerUser(
        email: String,
        password: String,
        name: String
    ): AsyncResponse<User<Map<String, Any>>?> {
        return try {
            val account = account.create(
                userId = ID.unique(),
                email = email,
                password = password,
                name = name,
            )
            // Must start a session to enter the users information in the User Table
            login(email, password)

            AsyncResponse.Success(
                data = account,
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
