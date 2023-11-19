package com.example.googlelightcalendar

import androidx.navigation.NavGraph
import assertk.assertThat
import assertk.assertions.isEqualTo
import com.example.googlelightcalendar.navigation.components.NavigationBuilder
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
    val result = NavigationBuilder.buildDestination(
            NavigationDestinations.registerScreen,
            mapOf("email" to "samuel")
        )

        assertThat(result.destination).isEqualTo("${NavigationDestinations.registerScreen.destination.replace("{email}", "samuel")}")
    }

    @Test
    fun experimentTest(){
        val result = NavigationBuilder.buildDestination(
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