package com.example.chooseu.data.rest.api_service.service.weight_history

import com.example.chooseu.data.rest.api_service.AppWriteConstants
import com.example.chooseu.utils.AsyncResponse
import com.google.gson.JsonObject
import io.appwrite.Client
import io.appwrite.ID
import io.appwrite.Query
import io.appwrite.exceptions.AppwriteException
import io.appwrite.models.Document
import io.appwrite.services.Databases

class BodyMassIndexRemoteService(client: Client) {
    companion object {
        private const val userCollectionId = "UserWeightHistory"
    }

    private val databases = Databases(client)

    suspend fun fetchUserWeightHistory(userId: String): Document<Map<String, Any>> {
        return databases.listDocuments(
            AppWriteConstants.userDatabaseId,
            userCollectionId,
            listOf(Query.equal("userId", "${userId}"), Query.limit(10))
        ).documents.first()
    }

    suspend fun add(
        userId: String,
        weight: Double,
        weightMetric: String,
        date: Long,
    ): Document<Map<String, Any>> {
        return databases.createDocument(
            AppWriteConstants.userDatabaseId,
            userCollectionId,
            ID.unique(),
            mapOf(
                "userId" to userId,
                "weight" to weight,
                "weightMetrics" to weightMetric,
                "date" to date
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