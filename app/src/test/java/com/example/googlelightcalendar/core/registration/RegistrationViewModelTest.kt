package com.example.googlelightcalendar.core.registration

import app.cash.turbine.test
import assertk.assertThat
import assertk.assertions.isEqualTo
import com.example.googlelightcalendar.core.registration.InitialRegistrationState.*
import com.example.googlelightcalendar.navigation.components.destinations.GeneralDestinations
import com.example.googlelightcalendar.navigation.components.AuthNavManager
import com.example.googlelightcalendar.repo.UserRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.MethodSource
import org.mockito.kotlin.mock
import org.mockito.kotlin.spy
import org.mockito.kotlin.times
import org.mockito.kotlin.verify
import java.util.stream.Stream

class RegistrationViewModelTest {

    private lateinit var registrationCache: UserRegistrationCache
    private lateinit var navigationManger: AuthNavManager
    private lateinit var viewModel: RegistrationViewModel
    private lateinit var userRepository: UserRepository
    private val testDispatcher = StandardTestDispatcher()
    private val externalScope = CoroutineScope(testDispatcher)

    @BeforeEach
    fun setUp() {
        registrationCache = UserRegistrationCacheImpl()
        navigationManger = spy(
            AuthNavManager(externalScope = externalScope)
        )
        userRepository = mock()
        viewModel = spy(
            RegistrationViewModel(
                registrationCache = registrationCache,
                navigationManger = navigationManger,
                userRepository = userRepository,
            )
        )
    }

    @Test
    fun `OnStoreCredentials completed credentials Success`() {
        val state = PersonalInformationState(
            firstName = "Samuel",
            lastName = "Brandenburg",
            email = "sam@gmail.com",
            password = "2312421213"
        )

        val viewModel = spy(
            RegistrationViewModel(
                registrationCache = registrationCache,
                navigationManger = navigationManger,
                userRepository = userRepository,
            )
        )

        viewModel.onStoreCredentials(state)

        verify(navigationManger, times(1)).navigate(GeneralDestinations.RegisterDetailsDestination)
    }


    @MethodSource("providesFailedPersonInformationState")
    @ParameterizedTest
    fun `OnStoreCredentials missing credentials Failed`(state: PersonalInformationState) = runTest {

        viewModel.onStoreCredentials(state)

        verify(navigationManger, times(0)).navigate(GeneralDestinations.RegisterDetailsDestination)

        viewModel.state.test {
            val stateAfterOnStoreCredentials = awaitItem()

            assertThat(stateAfterOnStoreCredentials.failedSignUp.value.isError).isEqualTo(true)
        }
    }


    @Test
    fun `reset test`() = runTest {
        val state = PersonalInformationState()
        viewModel.onStoreCredentials(state)

        viewModel.state.test {
            val stateAfterMethodCall = awaitItem()
            assertThat(stateAfterMethodCall.failedSignUp.value.isError).isEqualTo(true)

            viewModel.reset()
            val stateAfterReset = awaitItem()
            assertThat(stateAfterReset.failedSignUp.value.isError).isEqualTo(false)
        }
    }


    companion object{
        @JvmStatic
        fun providesFailedPersonInformationState(): Stream<PersonalInformationState> = Stream.of(
                PersonalInformationState(
                        firstName = "Samuel",
                        lastName = "Brandenburg",
                        email = "",
                        password = "2312421213"
                ),
                PersonalInformationState(
                        firstName = "Samuel",
                        lastName = "Brandenburg",
                        email = "",
                        password = ""
                ),
                PersonalInformationState(
                        firstName = "Samuel",
                        lastName = "",
                        email = "",
                        password = ""
                ),
                PersonalInformationState(
                        firstName = "",
                        lastName = "Brandenburg",
                        email = "",
                        password = "2312421213"
                ),
                PersonalInformationState(
                        firstName = "",
                        lastName = "Brandenburg",
                        email = "samuelebrandenburg12@gmail.com",
                        password = ""
                ),
                PersonalInformationState(
                        firstName = "Samuel",
                        lastName = "Brandenburg",
                        email = "samuelebrandenburg12",
                        password = "2312421213"
                )


        )
    }
}