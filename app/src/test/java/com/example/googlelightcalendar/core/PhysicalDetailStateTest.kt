package com.example.googlelightcalendar.core

import assertk.assertThat
import assertk.assertions.isEqualTo
import com.example.googlelightcalendar.core.registration.Genders
import com.example.googlelightcalendar.core.registration.state.HeightUnits
import com.example.googlelightcalendar.core.registration.state.PhysicalDetailState
import com.example.googlelightcalendar.core.registration.state.UnitsOfWeight
import com.example.googlelightcalendar.core.registration.state.UserHeight
import com.example.googlelightcalendar.core.registration.state.UserWeight
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
        assertThat(state.userHeight.value.heightType).isEqualTo(testArg.expectedUnits)
    }


    @MethodSource("provideWeightUnits")
    @ParameterizedTest
    fun updateWeightMetricsTest(
        testArg: WeightUnitState
    ) {
        val state = PhysicalDetailState.PhysicalDetails()

        state.updateWeightMetrics(testArg.unit)

        assertThat(state.userWeight.value.weightType).isEqualTo(testArg.expectedWeightType)
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
        val state = PhysicalDetailState.PhysicalDetails(
            initialUserHeight = UserHeight(
                height = "dsadsadsa"
            )
        )
        assertThat(state.containsValidHeight()).isEqualTo(false)
        assertThat(state.userHeight.value.height).isEqualTo("dsadsadsa")
    }

    @MethodSource("providesHeightTestArgs")
    @ParameterizedTest
    fun `contains valid height paramTest`(
        args: HeightTestArgs
    ) {
        val state = PhysicalDetailState.PhysicalDetails()

        state.updateHeightMetrics(args.heightType.type)
        state.updateHeight(args.height)

        assertThat(state.containsValidHeight()).isEqualTo(args.expectedResult)
    }

    @MethodSource("providesWeightTestArgs")
    @ParameterizedTest
    fun `contains valid weight paramTest`(
        args: WeightTestArgs
    ) {
        val state = PhysicalDetailState.PhysicalDetails()
        state.updateWeightMetrics(args.weightType.type)
        state.updateWeight(args.weight)
        assertThat(state.containsValidWeight()).isEqualTo(args.expectedResult)
    }

    @MethodSource("providesPhysicalDetails")
    @ParameterizedTest
    fun `complete Form test`(args: PhysicalDetailsArgs) {
        assertThat(args.state.completedForm()).assertThat(args.failsRequirements)
    }


    companion object {
        val validHeightFeet = UserHeight(
            height = "6",
            heightType = HeightUnits.Feet,
        )

        val validHeightCentimeters = UserHeight(
            height = "11",
            heightType = HeightUnits.Centimeter,
        )

        val validWeightPounds = UserWeight(
            weight = "160",
            weightType = UnitsOfWeight.Pounds,
        )
        val validWeightKilo = UserWeight(

        )

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
                date = provideDateToString(
                    minusYears = 10L
                ),
                expectedPassState = false
            ),
            //Makes sure that the first day of turnning 18 works
            BirthdayTestState(
                date = provideDateToString(
                    minusYears = 18L,
                    minusDay = 1L,
                ),
                expectedPassState = true
            ),
            //Should fail not yet 18
            BirthdayTestState(
                date = provideDateToString(
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
                HeightUnits.Feet.type,
                HeightUnits.Feet,
            ),

            HeightUnitState(
                HeightUnits.Centimeter.type,
                HeightUnits.Centimeter,
            ),
        )

        data class WeightUnitState(
            val unit: String,
            val expectedWeightType: UnitsOfWeight,
        )

        @JvmStatic
        fun provideWeightUnits(): Stream<WeightUnitState> = Stream.of(
            //Default weight
            WeightUnitState(
                unit = "",
                expectedWeightType = UnitsOfWeight.Kilo
            ),
            WeightUnitState(
                unit = UnitsOfWeight.Kilo.type,
                expectedWeightType = UnitsOfWeight.Kilo
            ),
            WeightUnitState(
                unit = UnitsOfWeight.Pounds.type,
                expectedWeightType = UnitsOfWeight.Pounds
            ),
        )


        data class HeightTestArgs(
            val height: String,
            val heightType: HeightUnits,
            val expectedResult: Boolean,
        )

        @JvmStatic
        fun providesHeightTestArgs(): Stream<HeightTestArgs> = Stream.of(
            HeightTestArgs(
                height = "5.9",
                heightType = HeightUnits.Feet,
                expectedResult = true
            ),
            HeightTestArgs(
                height = "-2.9",
                heightType = HeightUnits.Feet,
                expectedResult = false
            ),
            HeightTestArgs(
                height = "20.9",
                heightType = HeightUnits.Feet,
                expectedResult = false
            ),
            HeightTestArgs(
                height = "10.9",
                heightType = HeightUnits.Feet,
                expectedResult = false
            ),
            HeightTestArgs(
                height = "10.9",
                heightType = HeightUnits.Centimeter,
                expectedResult = true
            ),
            HeightTestArgs(
                height = "12",
                heightType = HeightUnits.Centimeter,
                expectedResult = true
            ),
            HeightTestArgs(
                height = "13",
                heightType = HeightUnits.Centimeter,
                expectedResult = true,
            ),
            HeightTestArgs(
                height = "-1.9",
                heightType = HeightUnits.Centimeter,
                expectedResult = false
            ),
            HeightTestArgs(
                height = "0.9",
                heightType = HeightUnits.Centimeter,
                expectedResult = true
            ),
            HeightTestArgs(
                height = "-0.9",
                heightType = HeightUnits.Centimeter,
                expectedResult = false
            ),
        )


        data class WeightTestArgs(
            val weight: String,
            val weightType: UnitsOfWeight,
            val expectedResult: Boolean
        )

        @JvmStatic
        fun providesWeightTestArgs(): Stream<WeightTestArgs> = Stream.of(
            WeightTestArgs(
                weight = "130",
                weightType = UnitsOfWeight.Pounds,
                expectedResult = true
            ),
            WeightTestArgs(
                weight = "530",
                weightType = UnitsOfWeight.Pounds,
                expectedResult = true
            ),
            WeightTestArgs(
                weight = "636",
                weightType = UnitsOfWeight.Pounds,
                expectedResult = true
            ),
            WeightTestArgs(
                weight = "0",
                weightType = UnitsOfWeight.Pounds,
                expectedResult = false
            ),
            WeightTestArgs(
                weight = "60",
                weightType = UnitsOfWeight.Pounds,
                expectedResult = false
            ),
            WeightTestArgs(
                weight = "60",
                weightType = UnitsOfWeight.Kilo,
                expectedResult = true
            ),
            WeightTestArgs(
                weight = "20",
                weightType = UnitsOfWeight.Kilo,
                expectedResult = false
            ),
            WeightTestArgs(
                weight = "639",
                weightType = UnitsOfWeight.Kilo,
                expectedResult = false
            ),
        )

        data class PhysicalDetailsArgs(
            val state: PhysicalDetailState.PhysicalDetails,
            val failsRequirements: Boolean,
        )

        @JvmStatic
        fun providesPhysicalDetails(): Stream<PhysicalDetailsArgs> = Stream.of(
            //Contains all values needed
            PhysicalDetailsArgs(
                state = PhysicalDetailState.PhysicalDetails(
                    initialUserHeight = validHeightCentimeters,
                    initialUserWeight = validWeightKilo,
                    initialGenders = Genders.Male,
                    initialBirthdate = provideDateToString(
                        minusYears = 12,
                        minusDay = 2,
                    )
                ),
                failsRequirements = false
            ),
            //Contains invalid height value
            PhysicalDetailsArgs(
                state = PhysicalDetailState.PhysicalDetails(
                    initialUserHeight = validHeightCentimeters.copy(
                        height = "60"
                    ),
                    initialUserWeight = validWeightKilo,
                    initialGenders = Genders.Male,
                    initialBirthdate = provideDateToString(
                        minusYears = 12,
                        minusDay = 2,
                    )
                ),
                failsRequirements = true
            ),
        )
    }
}


//Uses relevant dates to always make sure the test is working correctly
fun provideDateToString(
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
