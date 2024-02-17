package com.example.chooseu.data.rest.api_service.service.account

import io.appwrite.Client
import io.appwrite.ID
import io.appwrite.models.User
import io.appwrite.exceptions.AppwriteException
import io.appwrite.services.Account

class AccountService(client: Client) {
    private val account = Account(client)

    suspend fun getLoggedIn(): User<Map<String, Any>>? {
        return try {
            account.get()
        } catch (e: AppwriteException) {
            null
        }
    }

    suspend fun login(email: String, password: String): User<Map<String, Any>>? {
        return try {
            account.createEmailSession(email, password)
            getLoggedIn()
        } catch (e: AppwriteException) {
            null
        }
    }

    suspend fun register(email: String, password: String): User<Map<String, Any>>? {
        return try {
            account.create(ID.unique(), email, password)
            login(email, password)
        } catch (e: AppwriteException) {
            null
        }
    }

    suspend fun logout() {
        account.deleteSession("current")
    }
}
