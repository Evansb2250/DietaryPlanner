package com.example.chooseu.fakes

import com.example.chooseu.auth.Token
import com.example.chooseu.core.TokenManager
import com.example.chooseu.utils.TokenUtil
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