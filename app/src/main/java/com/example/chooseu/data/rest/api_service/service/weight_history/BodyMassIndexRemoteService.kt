package com.example.chooseu.data.rest.api_service.service.weight_history

import com.example.chooseu.data.rest.api_service.AppWriteConstants
import com.example.chooseu.utils.AsyncResponse
import com.google.gson.JsonObject
import io.appwrite.Client
import io.appwrite.ID
import io.appwrite.Query
import io.appwrite.exceptions.AppwriteException
import io.appwrite.models.Document
import io.appwrite.models.DocumentList
import io.appwrite.services.Databases

class BodyMassIndexRemoteService(client: Client) {
    companion object {
        private const val bodyMassCollectionId = "BodyMassTable"
    }

    private val databases = Databases(client)

    suspend fun fetchUserWeightHistory(userId: String): AsyncResponse<DocumentList <Map<String, Any>>>{
        return try {
            AsyncResponse.Success(
                data = databases.listDocuments(
                    AppWriteConstants.databaseId,
                    bodyMassCollectionId,
                    listOf(Query.equal("userId", "${userId}"), Query.limit(10))
                )
            )
        } catch (e: AppwriteException) {
            AsyncResponse.Failed(
                data = null,
                message = e.message ?: "failed to fetch weight history"
            )
        }
    }

    suspend fun createNewDocument(
        userId: String,
        weight: Double,
        weightMetric: String,
        height: Double,
        heightMetric: String,
        bmi: Double,
        dateInteger: Long,
    ): Document<Map<String, Any>> {
        return databases.createDocument(
            AppWriteConstants.databaseId,
            bodyMassCollectionId,
            ID.unique(),
            mapOf(
                "userId" to userId,
                "weight" to weight,
                "weightMetric" to weightMetric,
                "height" to height,
                "heightMetric" to heightMetric,
                "bmi" to bmi,
                "dateInteger" to dateInteger
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
                    AppWriteConstants.databaseId,
                    bodyMassCollectionId,
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
            AppWriteConstants.databaseId,
            bodyMassCollectionId,
            id
        )
    }
}