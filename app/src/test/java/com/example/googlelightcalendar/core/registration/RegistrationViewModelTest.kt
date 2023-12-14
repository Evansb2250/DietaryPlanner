package com.example.googlelightcalendar.core.registration

import app.cash.turbine.test
import assertk.assertThat
import assertk.assertions.isEqualTo
import com.example.googlelightcalendar.navigation.components.NavigationDestinations
import com.example.googlelightcalendar.navigation.components.NavigationManger
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.kotlin.spy
import org.mockito.kotlin.times
import org.mockito.kotlin.verify

class RegistrationViewModelTest {

    private lateinit var registrationCache: UserRegistrationCache
    private lateinit var navigationManger: NavigationManger
    private lateinit var viewModel: RegistrationViewModel
    private val testDispatcher = StandardTestDispatcher()
    private val externalScope = CoroutineScope(testDispatcher)

    @BeforeEach
    fun setUp() {
        registrationCache = UserRegistrationCacheImpl()
        navigationManger = spy(
            NavigationManger(externalScope = externalScope)
        )

        viewModel = spy(
            RegistrationViewModel(
                registrationCache = registrationCache,
                navigationManger = navigationManger,
            )
        )
    }

    @Test
    fun `OnStoreCredentials completed credentials Success`() {
        val state = InitialRegistrationState.PersonalInformationState(
            initialFirstName = "Samuel",
            initialLastName = "Brandenburg",
            initialEmail = "sam@gmail.com",
            initialPassword = "2312421213"
        )

        val viewModel = spy(
            RegistrationViewModel(
                registrationCache = registrationCache,
                navigationManger = navigationManger,
            )
        )

        viewModel.onStoreCredentials(state)

        verify(navigationManger, times(1)).navigate(NavigationDestinations.registerPhysicalScreen)
    }

    @Test
    fun `OnStoreCredentials missing credentials Failed`() = runTest {
        val state = InitialRegistrationState.PersonalInformationState(
            initialFirstName = "Samuel",
            initialLastName = "Brandenburg",
            initialEmail = "",
            initialPassword = "2312421213"
        )

        viewModel.onStoreCredentials(state)

        verify(navigationManger, times(0)).navigate(NavigationDestinations.registerPhysicalScreen)

        viewModel.state.test {
            val stateAfterOnStoreCredentials = awaitItem()

            assertThat(stateAfterOnStoreCredentials.failedSignUp.value.isError).isEqualTo(true)
        }
    }


    @Test
    fun `reset test`() = runTest {
        val state = InitialRegistrationState.PersonalInformationState()
        viewModel.onStoreCredentials(state)

        viewModel.state.test {
            val stateAfterMethodCall = awaitItem()
            assertThat(stateAfterMethodCall.failedSignUp.value.isError).isEqualTo(true)

            viewModel.reset()
            val stateAfterReset = awaitItem()
            assertThat(stateAfterReset.failedSignUp.value.isError).isEqualTo(false)
        }
    }
}