package com.example.googlelightcalendar.domain

import com.example.googlelightcalendar.data.room.database.models.UserEntity

data class User(
    val userName: String,
    val password: String,
)


fun User.toUserEntity(): UserEntity {
    return UserEntity(
        userName = this.userName,
        name = "",
        password = this.password
    )
}