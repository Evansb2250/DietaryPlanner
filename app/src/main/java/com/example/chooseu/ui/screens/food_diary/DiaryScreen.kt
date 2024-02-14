package com.example.chooseu.ui.screens.food_diary

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.chooseu.common.sidePadding
import com.example.chooseu.core.diary.DiaryScreenViewModel
import com.example.chooseu.core.diary.states.DiaryScreenStates
import com.example.chooseu.core.toolBarStates.ToolBarState
import com.example.chooseu.ui.theme.appColor
import com.example.chooseu.ui.ui_components.mealItem.MealItem
import com.example.chooseu.ui.ui_components.toolbar.ChooseUToolBar
import com.example.chooseu.utils.DateUtil

@Preview(
    showBackground = true,
)
@Composable
fun DiaryScreen(
    viewModel: DiaryScreenViewModel = hiltViewModel(),
) {
    BackHandler {

    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            ChooseUToolBar(
                toolBarState = ToolBarState.Home(),
                navigateBack = { /*TODO*/ },
                navigateToActionDestination = {}
            )
        }
    ) { it ->
        Column(
            modifier = Modifier.padding(it)
        ) {
            val state = viewModel.state.collectAsState().value

            when (state) {
                is DiaryScreenStates.MealDiary -> {
                    Header(
                   DateUtil.convertDateToString(state.date)
                    )
                    LazyColumn(
                        modifier = Modifier
                            .background(color = appColor)
                            .fillMaxSize()
                            .padding(sidePadding)
                    ) {
                        MealItem(
                            dayOfMonth = state.date.dayOfMonth.toString(),
                            menuItem = state.breakfast,
                            onAddFood = viewModel::addFoodItem,
                        )
                        MealItem(
                            dayOfMonth = state.date.dayOfMonth.toString(),
                            menuItem = state.lunch,
                            onAddFood = viewModel::addFoodItem,
                        )
                        MealItem(
                            dayOfMonth = state.date.dayOfMonth.toString(),
                            menuItem = state.dinner,
                            onAddFood = viewModel::addFoodItem,
                        )
                    }
                }
                else -> {}
            }

        }
    }
}
