package com.example.chooseu.data.rest.api_service.service.idea

import io.appwrite.Client
import io.appwrite.ID
import io.appwrite.Query
import io.appwrite.models.Document
import io.appwrite.services.Databases

class IdeaService(client: Client) {
    companion object {
        private const val ideaDatabaseId = "65cc1767ac6f56d136eb"
        private const val ideaCollectionId = "ideas-tracker"
    }

    private val databases = Databases(client)

    suspend fun fetch(userId: String): List<Document<Map<String, Any>>> {
        return databases.listDocuments(
            ideaDatabaseId,
            ideaCollectionId,
            listOf(Query.equal("userId", "${userId}"),  Query.limit(10))
        //    listOf(Query.orderDesc("\$createdAt"), Query.limit(10))
        ).documents
    }

    suspend fun add(
        userId: String,
        title: String,
        description: String
    ): Document<Map<String, Any>> {
        return databases.createDocument(
            ideaDatabaseId,
            ideaCollectionId,
            ID.unique(),
            mapOf(
                "userId" to userId,
                "title" to title,
                "description" to description
            )
        )
    }

    suspend fun remove(id: String) {
        databases.deleteDocument(
            ideaDatabaseId,
            ideaCollectionId,
            id
        )
    }
}