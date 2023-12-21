package com.example.googlelightcalendar.core

import assertk.assertThat
import assertk.assertions.isEqualTo
import com.example.googlelightcalendar.core.registration.Genders
import com.example.googlelightcalendar.core.registration.HeightUnits
import com.example.googlelightcalendar.core.registration.PhysicalDetailState
import com.example.googlelightcalendar.core.registration.UnitsInWeight
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.MethodSource
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.stream.Stream

class PhysicalDetailStateTest {

    @MethodSource("providesBirthDays")
    @ParameterizedTest
    fun containsValidBirthdayTest(testArg: BirthdayTestState) {
        val state = PhysicalDetailState.PhysicalDetails().apply {
            birthDate.value = testArg.date
        }
        assertThat(state.containsValidBirthday()).isEqualTo(testArg.expectedPassState)
    }

    @MethodSource("provideHeightUnits")
    @ParameterizedTest
    fun updateHeightMetricsTest(
        testArg: HeightUnitState
    ) {
        val state = PhysicalDetailState.PhysicalDetails()

        state.updateHeightMetrics(testArg.unit)

        assertThat(state.heightUnit.value).isEqualTo(testArg.expectedUnits)
    }


    @MethodSource("provideWeightUnits")
    @ParameterizedTest
    fun updateWeightMetricsTest(
        testArg: WeightUnitState
    ) {
        val state = PhysicalDetailState.PhysicalDetails()

        state.updateWeightMetrics(testArg.unit)

        assertThat(state.weightUnit.value).isEqualTo(testArg.expectedUnits)
    }

    @Test
    fun updateWeightMetricsTest() {
        val state = PhysicalDetailState.PhysicalDetails()
        assertThat(state.selectedAGender()).isEqualTo(false)
        state.selectedGender.value = Genders.Male
        assertThat(state.selectedAGender()).isEqualTo(true)
    }

    @Test
    fun `contains valid height on initialization failed`() {
        val state = PhysicalDetailState.PhysicalDetails()
        assertThat(state.containsValidHeight()).isEqualTo(false)
    }

    @Test
    fun `contains valid height throws NumberFormatException`() {
        val state = PhysicalDetailState.PhysicalDetails()
        state.height.value = "dsadsadsa"
        assertThat(state.containsValidHeight()).isEqualTo(false)
        assertThat(state.height.value).isEqualTo("dsadsadsa")
    }

    @MethodSource("providesHeightTestArgs")
    @ParameterizedTest
    fun `contains valid height paramTest`(
        args: HeightTestArgs
    ) {
        val state = PhysicalDetailState.PhysicalDetails().apply {
            this.height.value = args.height
        }

        state.updateHeightMetrics(args.unit.unit)

        assertThat(state.containsValidHeight()).isEqualTo(args.expectedResult)
    }

    @MethodSource("providesWeightTestArgs")
    @ParameterizedTest
    fun `contains valid weight paramTest`(
        args: WeightTestArgs
    ) {
        val state = PhysicalDetailState.PhysicalDetails().apply {
            this.weight.value = args.weight
        }

        state.updateWeightMetrics(args.weightType.unit)

        assertThat(state.containsValidWeight()).isEqualTo(args.expectedResult)
    }

    @MethodSource("providesPhysicalDetails")
    @ParameterizedTest
    fun `complete Form test`(args: PhysicalDetailsArgs) {
        assertThat(args.state.completedForm()).assertThat(args.expectedResult)
    }


    companion object {

        data class BirthdayTestState(
            val date: String,
            val expectedPassState: Boolean,
        )

        @JvmStatic
        fun providesBirthDays(): Stream<BirthdayTestState> = Stream.of(
            BirthdayTestState(
                "12/19/2005",
                true,
            ),
            BirthdayTestState(
                date = provideDateByString(
                    minusYears = 10L
                ),
                expectedPassState = false
            ),
            //Makes sure that the first day of turnning 18 works
            BirthdayTestState(
                date = provideDateByString(
                    minusYears = 18L,
                    minusDay = 1L,
                ),
                expectedPassState = true
            ),
            //Should fail not yet 18
            BirthdayTestState(
                date = provideDateByString(
                    minusYears = 17L,
                ),
                expectedPassState = false
            )
        )

        data class HeightUnitState(
            val unit: String,
            val expectedUnits: HeightUnits,
        )

        @JvmStatic
        fun provideHeightUnits(): Stream<HeightUnitState> = Stream.of(
            HeightUnitState(
                "",
                HeightUnits.Feet,
            ),
            HeightUnitState(
                HeightUnits.Feet.unit,
                HeightUnits.Feet,
            ),

            HeightUnitState(
                HeightUnits.Centermeters.unit,
                HeightUnits.Centermeters,
            ),
        )

        data class WeightUnitState(
            val unit: String,
            val expectedUnits: UnitsInWeight,
        )

        @JvmStatic
        fun provideWeightUnits(): Stream<WeightUnitState> = Stream.of(
            //Default weight
            WeightUnitState(
                unit = "",
                expectedUnits = UnitsInWeight.Kilo
            ),
            WeightUnitState(
                unit = UnitsInWeight.Kilo.unit,
                expectedUnits = UnitsInWeight.Kilo
            ),
            WeightUnitState(
                unit = UnitsInWeight.Pounds.unit,
                expectedUnits = UnitsInWeight.Pounds
            ),
        )


        data class HeightTestArgs(
            val height: String,
            val unit: HeightUnits,
            val expectedResult: Boolean,
        )

        @JvmStatic
        fun providesHeightTestArgs(): Stream<HeightTestArgs> = Stream.of(
            HeightTestArgs(
                height = "5.9",
                unit = HeightUnits.Feet,
                expectedResult = true
            ),
            HeightTestArgs(
                height = "-2.9",
                unit = HeightUnits.Feet,
                expectedResult = false
            ),
            HeightTestArgs(
                height = "20.9",
                unit = HeightUnits.Feet,
                expectedResult = false
            ),
            HeightTestArgs(
                height = "10.9",
                unit = HeightUnits.Feet,
                expectedResult = false
            ),
            HeightTestArgs(
                height = "10.9",
                unit = HeightUnits.Centermeters,
                expectedResult = true
            ),
            HeightTestArgs(
                height = "12",
                unit = HeightUnits.Centermeters,
                expectedResult = true
            ),
            HeightTestArgs(
                height = "13",
                unit = HeightUnits.Centermeters,
                expectedResult = true,
            ),
            HeightTestArgs(
                height = "-1.9",
                unit = HeightUnits.Centermeters,
                expectedResult = false
            ),
            HeightTestArgs(
                height = "0.9",
                unit = HeightUnits.Centermeters,
                expectedResult = true
            ),
            HeightTestArgs(
                height = "-0.9",
                unit = HeightUnits.Centermeters,
                expectedResult = false
            ),
        )


        data class WeightTestArgs(
            val weight: String,
            val weightType: UnitsInWeight,
            val expectedResult: Boolean
        )

        @JvmStatic
        fun providesWeightTestArgs(): Stream<WeightTestArgs> = Stream.of(
            WeightTestArgs(
                weight = "130",
                weightType = UnitsInWeight.Pounds,
                expectedResult = true
            ),
            WeightTestArgs(
                weight = "530",
                weightType = UnitsInWeight.Pounds,
                expectedResult = true
            ),
            WeightTestArgs(
                weight = "636",
                weightType = UnitsInWeight.Pounds,
                expectedResult = true
            ),
            WeightTestArgs(
                weight = "0",
                weightType = UnitsInWeight.Pounds,
                expectedResult = false
            ),
            WeightTestArgs(
                weight = "60",
                weightType = UnitsInWeight.Pounds,
                expectedResult = false
            ),
            WeightTestArgs(
                weight = "60",
                weightType = UnitsInWeight.Kilo,
                expectedResult = true
            ),
            WeightTestArgs(
                weight = "20",
                weightType = UnitsInWeight.Kilo,
                expectedResult = false
            ),
            WeightTestArgs(
                weight = "639",
                weightType = UnitsInWeight.Kilo,
                expectedResult = false
            ),
        )

        data class PhysicalDetailsArgs(
            val state: PhysicalDetailState.PhysicalDetails,
            val expectedResult: Boolean,
        )

        @JvmStatic
        fun providesPhysicalDetails(): Stream<PhysicalDetailsArgs> = Stream.of(
            PhysicalDetailsArgs(
                state = PhysicalDetailState.PhysicalDetails(),
                expectedResult = false,
            ),
        )
    }
}


//Uses relevant dates to always make sure the test is working correctly
fun provideDateByString(
    minusYears: Long = 0,
    minusMonths: Long = 0,
    minusDay: Long = 0,
): String {
    val currentDate = LocalDateTime
        .now()
        .minusMonths(minusMonths)
        .minusDays(minusDay)
        .minusYears(minusYears)

    val dateFormater = DateTimeFormatter.ofPattern("MM/dd/YYYY")

    return currentDate.format(dateFormater)
}
