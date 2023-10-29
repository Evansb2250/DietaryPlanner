package com.example.googlelightcalendar.data.room.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.example.googlelightcalendar.data.room.database.models.UserEntity

@Dao
interface UserDao {
    @Insert
    suspend fun insertUser(newUser: UserEntity)

    @Query("Select * From USERENTITY where userName=:emailName AND password=:password  ")
    suspend fun getUser(
        emailName: String,
        password: String,
    ): UserEntity?

    @Delete
    suspend fun deleteUser(
        userEntity: UserEntity
    )
}