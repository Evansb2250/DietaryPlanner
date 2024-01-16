package com.example.googlelightcalendar

import assertk.assertThat
import assertk.assertions.isEqualTo
import com.example.googlelightcalendar.navigation.components.NavigationDestinations
import org.junit.jupiter.api.Test

class NavigationDestinationTest {

    @Test
    fun loginScreenDestinationTest(){
        NavigationDestinations.loginScreen.destination

        assertThat(NavigationDestinations.loginScreen.destination).isEqualTo("loginScreen")
    }

    @Test
    fun registerScreenDestinationTest(){
    val result = NavigationDestinations.buildDestination(
            NavigationDestinations.registerScreen,
            mapOf("email" to "samuel")
        )

        assertThat(result.destination).isEqualTo("${NavigationDestinations.registerScreen.destination.replace("{email}", "samuel")}")
    }

    @Test
    fun experimentTest(){
        val result = NavigationDestinations.buildDestination(
            NavigationDestinations.experiment,
            mapOf(
                "email" to "samuel@gmail.com",
                "id" to "23",
                "name" to "Jerry"
            )

        )

        assertThat(result.destination).isEqualTo("experiment/samuel@gmail.com/23/Jerry")
    }
}