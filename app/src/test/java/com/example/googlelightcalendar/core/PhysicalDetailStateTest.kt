package com.example.googlelightcalendar.core

import assertk.assertThat
import assertk.assertions.isEqualTo
import com.example.googlelightcalendar.core.registration.PhysicalDetailState
import org.junit.jupiter.api.Test

class PhysicalDetailStateTest {

    @Test
    fun containsValidBirthdayTest() {
        val state = PhysicalDetailState.PhysicalDetails().apply {
            birthDate.value = "12/19/2005"
        }
        assertThat(state.containsValidBirthday()).isEqualTo(false)
    }
}