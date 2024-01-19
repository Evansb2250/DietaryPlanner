package com.example.googlelightcalendar

import assertk.assertThat
import assertk.assertions.isEqualTo
import com.example.googlelightcalendar.navigation.components.NavigationDestinations
import com.example.googlelightcalendar.utils.buildDestination
import org.junit.jupiter.api.Test

class NavigationDestinationTest {

    @Test
    fun loginScreenDestinationTest(){
        NavigationDestinations.loginScreen.destination

        assertThat(NavigationDestinations.loginScreen.destination).isEqualTo("loginScreen")
    }

    @Test
    fun registerScreenDestinationTest(){
    val result = buildDestination(
            NavigationDestinations.RegisterScreen,
            mapOf("email" to "samuel")
        )

        assertThat(result.destination).isEqualTo("${NavigationDestinations.RegisterScreen.destination.replace("{email}", "samuel")}")
    }
}