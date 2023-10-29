package com.example.googlelightcalendar.viewmodels

import com.example.googlelightcalendar.core.viewmodels.login.LoginViewModel
import com.example.googlelightcalendar.data.room.database.dao.UserDao
import com.example.googlelightcalendar.fakes.UserDaoFake
import com.example.googlelightcalendar.fakes.UserRepositoryFake
import com.example.googlelightcalendar.repo.UserRepository
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@RunWith(JUnit4::class)
class LoginViewModelTest {

    private lateinit var userRespositoryFake: UserRepositoryFake
    private lateinit var loginViewModel: LoginViewModel
    private lateinit var userDaoFake: UserDao
    @BeforeEach
    fun setUp(){
        userDaoFake = UserDaoFake()
        userRespositoryFake = UserRepositoryFake(
            userDaoFake
        )
        loginViewModel = LoginViewModel(
            userRepository = userRespositoryFake
        )
    }

    @Test
    fun signIn(){
       loginViewModel.signInWithGoogle()
        userRespositoryFake
    }

    @Test
    fun createAccountManually(){

    }

    @Test
    fun attemptLogin(){

    }

    @Test
    fun verifyCorrectEmailAddress(){

    }
    @Test
    fun verifyCorrectPasswordTest(){

    }

    @Test
    fun signInWithFaceBook(){

    }

    @Test
    fun signInWithGoogle(){

    }

}