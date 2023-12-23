package com.example.googlelightcalendar.core.registration

import app.cash.turbine.test
import assertk.assertThat
import assertk.assertions.isEqualTo
import com.example.googlelightcalendar.core.PhysicalDetailStateTest
import com.example.googlelightcalendar.core.provideDateToString
import com.example.googlelightcalendar.core.registration.state.PhysicalDetailState
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

class PhysicalDetailsViewModelTest {
    private lateinit var navigationManager: NavigationManger
    private lateinit var userRegistrationCache: UserRegistrationCache
    private lateinit var viewModel: PhysicalDetailsViewModel
    private val testDispatcher = StandardTestDispatcher()
    private lateinit var scope: CoroutineScope


    @BeforeEach
    fun setUp() {
        scope = CoroutineScope(testDispatcher)
        navigationManager = spy(NavigationManger(scope))
        userRegistrationCache = spy(UserRegistrationCacheImpl())

        viewModel = PhysicalDetailsViewModel(
            navigationManger = navigationManager,
            cache = userRegistrationCache,
        )
    }


    @Test
    fun storePhysicalDetailsInCacheTestPass() {
        val state = provideCompletedDetailsState()
        viewModel.storePhysicalDetailsInCache(state)
        verify(navigationManager, times(1)).navigate(NavigationDestinations.registerGoalsScreen)
    }

    @Test
    fun storePhysicalDetailsInCacheTestFailed() = runTest{
        val state = provideCompletedDetailsState().apply {
            this.birthDate.value = ""
        }
        viewModel.storePhysicalDetailsInCache(state)
        verify(navigationManager, times(0)).navigate(NavigationDestinations.registerGoalsScreen)
        viewModel.state.test {
            val state = awaitItem()
            assertThat(state.errorState.isError).isEqualTo(true)
        }
    }


    @Test
    fun storePhysicalDetailsInCacheResetAfterFailure() = runTest{
        val state = provideCompletedDetailsState().apply {
            this.birthDate.value = ""
        }
        viewModel.storePhysicalDetailsInCache(state)
        verify(navigationManager, times(0)).navigate(NavigationDestinations.registerGoalsScreen)
        viewModel.state.test {
            val state = awaitItem()
            assertThat(state.errorState.isError).isEqualTo(true)
            viewModel.reset()

            val stateAfterReset = awaitItem()

            assertThat(stateAfterReset.errorState.isError).isEqualTo(false)
        }
    }



    fun provideCompletedDetailsState(): PhysicalDetailState.PhysicalDetails =
        PhysicalDetailState.PhysicalDetails(
            initialUserHeight = PhysicalDetailStateTest.validHeightCentimeters.copy(
                height = "60"
            ),
            initialUserWeight = PhysicalDetailStateTest.validWeightPounds,
            initialGenders = Genders.Male,
            initialBirthdate = provideDateToString(
                minusYears = 19,
                minusDay = 2,
            )
        )
}