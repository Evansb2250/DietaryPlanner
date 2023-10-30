package com.example.googlelightcalendar.fakes

import com.example.googlelightcalendar.auth.Token
import com.example.googlelightcalendar.auth.TokenType
import com.example.googlelightcalendar.core.TokenManager
import kotlinx.coroutines.flow.Flow

class TokenManagerFakeImpl: TokenManager {
    val dataStore = hashMapOf<String, Any>()
    override suspend fun getTokenType(token: TokenType): Token {
        TODO("Not yet implemented")
    }

    override suspend fun saveToken(key: Token, token: String) {
        TODO("Not yet implemented")
    }

    override suspend fun <T : Token> fetchToken(key: T): Flow<String?> {
        TODO("Not yet implemented")
    }

}