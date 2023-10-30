package com.example.googlelightcalendar.fakes

import com.example.googlelightcalendar.data.room.database.dao.UserDao
import com.example.googlelightcalendar.data.room.database.models.UserEntity

class UserDaoFake : UserDao {
    private val inMemeryUserDao = hashMapOf<String, UserEntity>()
    override suspend fun insertUser(newUser: UserEntity) {
        inMemeryUserDao[newUser.userName] = newUser
    }

    override suspend fun getUser(emailName: String, password: String): UserEntity? =
        inMemeryUserDao.values.find { userEntity ->
            userEntity.userName == emailName && userEntity.password == password
        }

    override suspend fun deleteUser(userEntity: UserEntity) {
        inMemeryUserDao.remove(userEntity.userName)
    }
}