package com.example.chooseu.data.rest.api_service.service.user

import io.appwrite.Client
import io.appwrite.ID
import io.appwrite.Query
import io.appwrite.models.Document
import io.appwrite.services.Databases

class UserService(client: Client) {
    companion object {
        private const val ideaDatabaseId = "65cc1767ac6f56d136eb"
        private const val userCollectionId = "UserTable"
    }

    private val databases = Databases(client)

    suspend fun fetch(userId: String): List<Document<Map<String, Any>>> {
        return databases.listDocuments(
            ideaDatabaseId,
            userCollectionId,
            listOf(Query.equal("userId", "${userId}"), Query.limit(10))
            //    listOf(Query.orderDesc("\$createdAt"), Query.limit(10))
        ).documents
    }

    suspend fun add(
        userId: String,
        firstName: String,
        lastName: String,
        height: Double,
        heightMetric: String,
        weight: Double,
        weightMetric: String,
        email: String,
        gender: String
    ): Document<Map<String, Any>> {
        return databases.createDocument(
            ideaDatabaseId,
            userCollectionId,
            ID.unique(),
            mapOf(
                "userId" to userId,
                "firstName" to firstName,
                "lastName" to lastName,
                "Height" to height,
                "heightMetric" to heightMetric,
                "weight" to weight.toInt(),
                "weightMetric" to weightMetric,
                "email" to email,
                "gender" to gender,
            )
        )
    }

    suspend fun remove(id: String) {
        databases.deleteDocument(
            ideaDatabaseId,
            userCollectionId,
            id
        )
    }
}