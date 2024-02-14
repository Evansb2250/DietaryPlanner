package com.example.chooseu.utils

import com.example.chooseu.auth.Token
import com.example.chooseu.auth.TokenType

object TokenUtil {
    fun getTokenKey(key: Token): String {
        return when (key) {
            Token.GoogleToken.IdToken -> {
                Token.GoogleToken.IdToken.tokenName
            }

            Token.GoogleToken.AccessToken -> {
                Token.GoogleToken.AccessToken.tokenName
            }

            Token.GoogleToken.ExpirationToken -> {
                Token.GoogleToken.ExpirationToken.tokenName
            }
        }
    }


    fun getTokenType(token: TokenType): Token {
        return when(token){
            TokenType.Access -> Token.GoogleToken.AccessToken
            TokenType.Expiration -> Token.GoogleToken.ExpirationToken
            TokenType.ID -> Token.GoogleToken.IdToken
        }
    }
}