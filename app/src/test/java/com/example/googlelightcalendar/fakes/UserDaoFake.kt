package com.example.googlelightcalendar.fakes

import com.example.googlelightcalendar.data.room.database.dao.UserDao
import com.example.googlelightcalendar.data.room.database.models.UserEntity

class UserDaoFake : UserDao {
    private val inMemoryUserDao = hashMapOf<String, UserEntity>()
    override suspend fun insertUser(newUser: UserEntity) {
        inMemoryUserDao[newUser.userName] = newUser
    }

    override suspend fun getUser(emailName: String, password: String): UserEntity? =
        inMemoryUserDao.values.find { userEntity ->
            userEntity.userName == emailName && userEntity.password == password
        }

    override fun getUserFromGmailSignIn(gmail: String): UserEntity? {
        return  inMemoryUserDao.filter {  it.value.userName == gmail }.getOrDefault(gmail, null)
    }

    override suspend fun deleteUser(userEntity: UserEntity) {
        inMemoryUserDao.remove(userEntity.userName)
    }
}