package com.example.googlelightcalendar.domain

import com.example.googlelightcalendar.data.room.database.models.UserEntity

data class User(
    val userName: String,
    val name: String,
)


fun User.toUserEntity(): UserEntity {
    return UserEntity(
        userName = this.userName,
        password = "",
        name = this.name,
    )
}