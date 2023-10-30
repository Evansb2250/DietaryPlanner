package com.example.googlelightcalendar.repo

import com.example.googlelightcalendar.auth.OauthClientImp
import com.example.googlelightcalendar.core.GoogleTokenManagerImpl
import com.example.googlelightcalendar.data.room.database.dao.UserDao

class UserRepositoryImplTest {
    private lateinit var repository: UserRepository
    private lateinit var googleOauthClient: OauthClientImp
    private lateinit var userDao: UserDao
    private lateinit var tokenManager: GoogleTokenManagerImpl



}