package com.example.googlelightcalendar.auth

sealed class TokenType{
    object Access: TokenType()
    object ID: TokenType()
    object Expiration: TokenType()
}