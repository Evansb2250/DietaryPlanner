package com.example.chooseu

import assertk.assertThat
import assertk.assertions.isEqualTo
import com.example.chooseu.navigation.components.destinations.GeneralDestinations
import com.example.chooseu.utils.buildDestination
import org.junit.jupiter.api.Test

class NavigationDestinationTest {

    @Test
    fun loginScreenDestinationTest(){
        GeneralDestinations.loginScreen.destination

        assertThat(GeneralDestinations.loginScreen.destination).isEqualTo("loginScreen")
    }

    @Test
    fun registerScreenDestinationTest(){
    val result = buildDestination(
            GeneralDestinations.RegisterScreen,
            mapOf("email" to "samuel")
        )

        assertThat(result.destination).isEqualTo("${GeneralDestinations.RegisterScreen.destination.replace("{email}", "samuel")}")
    }
}