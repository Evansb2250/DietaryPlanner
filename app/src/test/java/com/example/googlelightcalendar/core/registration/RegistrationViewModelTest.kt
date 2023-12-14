package com.example.googlelightcalendar.core.registration

import com.example.googlelightcalendar.navigation.components.NavigationManger
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

/**
 *  Things I need to to test
 *
 *  1. testing method onStoreCredentials
 *  2. testing navigateNextPage()
 *  3. test reset()
 *  4.
 */
class RegistrationViewModelTest {

   private lateinit var registrationCache: UserRegistrationCache
   private lateinit var navigationManger: NavigationManger
   @BeforeEach
   fun setUp(){

   }

    @Test
    fun viewModelTest(){

        val viewModel = RegistrationViewModel(
            registrationCache = registrationCache,
            navigationManger = navigationManger,
        )
    }



}