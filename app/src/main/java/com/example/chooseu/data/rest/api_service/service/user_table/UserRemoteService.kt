package com.example.chooseu.data.rest.api_service.service.user_table

import com.example.chooseu.data.rest.api_service.AppWriteConstants
import com.example.chooseu.utils.AsyncResponse
import com.google.gson.JsonObject
import io.appwrite.Client
import io.appwrite.ID
import io.appwrite.Query
import io.appwrite.exceptions.AppwriteException
import io.appwrite.models.Document
import io.appwrite.services.Databases

class UserRemoteService(client: Client) {
    companion object {

        private const val userCollectionId = "UserTable"
    }

    private val databases = Databases(client)

    suspend fun fetchUserDetails(userId: String): Document<Map<String, Any>> {
        return databases.listDocuments(
            AppWriteConstants.userDatabaseId,
            userCollectionId,
            listOf(Query.equal("userId", "${userId}"), Query.limit(10))
        ).documents.first()
    }

    suspend fun add(
        userId: String,
        firstName: String,
        lastName: String,
        birthDate: String,
        height: Double,
        heightMetric: String,
        weight: Double,
        weightMetric: String,
        email: String,
        gender: String
    ): Document<Map<String, Any>> {
        return databases.createDocument(
            AppWriteConstants.userDatabaseId,
            userCollectionId,
            ID.unique(),
            mapOf(
                "userId" to userId,
                "firstName" to firstName,
                "lastName" to lastName,
                "height" to height,
                "birthDate" to birthDate,
                "heightMetric" to heightMetric,
                "weight" to weight,
                "weightMetric" to weightMetric,
                "email" to email,
                "gender" to gender,
            )
        )
    }

    suspend fun updateDocument(
        documentId: String,
        data: JsonObject
    ): AsyncResponse<Document<Map<String, Any>>> {
        return try {
            AsyncResponse.Success(
                data = databases.updateDocument(
                    AppWriteConstants.userDatabaseId,
                    userCollectionId,
                    documentId = documentId,
                    data = data,
                )
            )
        } catch (e: AppwriteException) {
            AsyncResponse.Failed(data = null, message = e.message)
        }
    }

    suspend fun remove(id: String) {
        databases.deleteDocument(
            AppWriteConstants.userDatabaseId,
            userCollectionId,
            id
        )
    }
}