package com.example.googlelightcalendar.utils

import com.example.googlelightcalendar.auth.Token

fun getTokenKeyType(key: Token): String {
    return when (key) {
        Token.GoogleToken.IdToken -> {
            Token.GoogleToken.IdToken.googleTokenType
        }

        Token.GoogleToken.AccessToken -> {
            Token.GoogleToken.AccessToken.googleTokenType
        }

        Token.GoogleToken.ExpirationToken -> {
            Token.GoogleToken.ExpirationToken.googleTokenType
        }
    }
}