package com.example.googlelightcalendar.data.database.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.googlelightcalendar.domain.User

@Entity
data class UserEntity(
    @PrimaryKey
    val userName: String,
    val name: String,
    val lastName: String,
    val password: String
)


fun UserEntity.toUser(): User = User(
    userName = this.userName,
    name = this.name,
)