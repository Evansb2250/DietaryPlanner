package com.example.chooseu.utils

import com.example.chooseu.auth.Token
import com.example.chooseu.auth.TokenType
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter

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
    fun isTokenExpired(expirationDate: String): Boolean{
        val formatter = DateTimeFormatter.ISO_OFFSET_DATE_TIME
        val tokenDateTime = ZonedDateTime.parse(expirationDate, formatter)
        val currentDateTime = ZonedDateTime.now()
        return currentDateTime.isAfter(tokenDateTime)
    }



    fun getTokenType(token: TokenType): Token {
        return when(token){
            TokenType.Access -> Token.GoogleToken.AccessToken
            TokenType.Expiration -> Token.GoogleToken.ExpirationToken
            TokenType.ID -> Token.GoogleToken.IdToken
        }
    }
}