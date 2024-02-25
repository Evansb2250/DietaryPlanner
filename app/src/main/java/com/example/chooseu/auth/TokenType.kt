package com.example.chooseu.auth

sealed class TokenType{
    object Access: TokenType()
    object ID: TokenType()
    object Expiration: TokenType() //TODO("Update this to reflect that we are holding a date and not a token")
}