package com.example.chooseu.data.database.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.chooseu.domain.User

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