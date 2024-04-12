package com.example.chooseu.core.diary

import androidx.lifecycle.ViewModel
import com.example.chooseu.core.diary.states.DiaryScreenStates
import com.example.chooseu.navigation.components.destinations.GeneralDestinations
import com.example.chooseu.navigation.components.destinations.destinationArguments.DiaryArgs
import com.example.chooseu.navigation.components.navmanagers.AppNavManager
import com.example.chooseu.utils.DateUtility
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import java.time.LocalDate
import javax.inject.Inject


@HiltViewModel
class DiaryScreenViewModel @Inject constructor(
    val navigationManager: AppNavManager,
) : ViewModel() {

    private val _state: MutableStateFlow<DiaryScreenStates> = MutableStateFlow(
        DiaryScreenStates.FoodDiaryEntry(localDate = LocalDate.now())
    )

    val state = _state.asStateFlow()

    fun getPreviousDate(date: DiaryScreenStates.FoodDiaryEntry) {
        DateUtility.convertStringToDate(date.date)?.let { dateTime ->
            _state.update {
                date.copy(localDate = DateUtility.getPreviousDay(dateTime))
            }
        }
    }

    fun getNextDate(date: DiaryScreenStates.FoodDiaryEntry) {
        DateUtility.convertStringToDate(date.date)?.let { dateTime ->
            _state.update {
                date.copy(localDate = DateUtility.getNextDayAsDate(dateTime))
            }
        }
    }


    fun setUpScreenState(date: Long? = null) {
        _state.update {
            if (date != null) {
                DiaryScreenStates.FoodDiaryEntry(
                    localDate = DateUtility.convertDateLongToLocalDate(date)
                )
            } else {
                DiaryScreenStates.FoodDiaryEntry()
            }
        }
    }



    fun addFoodItem(
        date: String,
        foodType: String,
    ) {

        val dateAsLong = DateUtility.convertStringStringDateToLong(date)

        navigationManager.navigate(
            GeneralDestinations.FoodSearchFlow,
            arguments = mapOf(
                DiaryArgs.LONG_DATE.name to "$dateAsLong",
                DiaryArgs.MEAL_TYPE.name to foodType,
            )
        )
    }
}