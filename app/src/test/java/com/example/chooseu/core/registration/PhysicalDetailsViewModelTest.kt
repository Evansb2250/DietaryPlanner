package com.example.chooseu.core.registration

import app.cash.turbine.test
import assertk.assertThat
import assertk.assertions.isEqualTo
import com.example.chooseu.core.PhysicalDetailStateTest
import com.example.chooseu.core.provideDateToString
import com.example.chooseu.core.registration.cache.keys.RegistrationKeys
import com.example.chooseu.core.registration.cache.UserRegistrationCache
import com.example.chooseu.core.registration.cache.UserRegistrationCacheImpl
import com.example.chooseu.core.registration.state.PhysicalDetailState
import com.example.chooseu.navigation.components.destinations.GeneralDestinations
import com.example.chooseu.navigation.components.AuthNavManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.kotlin.spy
import org.mockito.kotlin.times
import org.mockito.kotlin.verify

class PhysicalDetailsViewModelTest {
    private lateinit var navigationManager: AuthNavManager
    private lateinit var userRegistrationCache: UserRegistrationCache
    private lateinit var viewModel: PhysicalDetailsViewModel
    private val testDispatcher = StandardTestDispatcher()
    private lateinit var scope: CoroutineScope


    @BeforeEach
    fun setUp() {
        scope = CoroutineScope(testDispatcher)
        navigationManager = spy(AuthNavManager(scope))
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
        verify(navigationManager, times(1)).navigate(GeneralDestinations.RegisterGoalsFlow)
        verify(userRegistrationCache, times(1)).storeKey(RegistrationKeys.BIRTHDATE, state.birthDate.value)
        assertThat(userRegistrationCache.getKey(RegistrationKeys.BIRTHDATE)).isEqualTo(state.birthDate.value)
    }

    @Test
    fun storePhysicalDetailsInCacheTestFailed() = runTest{
        val state = provideCompletedDetailsState().apply {
            this.birthDate.value = ""
        }
        viewModel.storePhysicalDetailsInCache(state)
        verify(navigationManager, times(0)).navigate(GeneralDestinations.RegisterGoalsFlow)
        viewModel.state.test {
            val stateAfterStoringCache = awaitItem()
            assertThat(stateAfterStoringCache.errorState.isError).isEqualTo(true)
        }
    }


    @Test
    fun storePhysicalDetailsInCacheResetAfterFailure() = runTest{
        val state = provideCompletedDetailsState().apply {
            this.birthDate.value = ""
        }
        viewModel.storePhysicalDetailsInCache(state)
        verify(navigationManager, times(0)).navigate(GeneralDestinations.RegisterGoalsFlow)
        viewModel.state.test {

            val stateAfterStoringCache = awaitItem()
            assertThat(stateAfterStoringCache.errorState.isError).isEqualTo(true)

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