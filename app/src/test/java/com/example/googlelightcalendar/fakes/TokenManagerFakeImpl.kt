package com.example.googlelightcalendar.fakes

import com.example.googlelightcalendar.auth.Token
import com.example.googlelightcalendar.core.TokenManager
import com.example.googlelightcalendar.utils.TokenUtil
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class TokenManagerFakeImpl : TokenManager {
    val dataStore = hashMapOf<String, String>()

    override suspend fun saveToken(key: Token, token: String) {
        dataStore[key.tokenType] = token
    }

    override suspend fun <T : Token> fetchToken(key: T): Flow<String?> {
        return flow {
            val tokenType = TokenUtil.getTokenKey(key)
            emit(dataStore[tokenType])
        }
    }

}