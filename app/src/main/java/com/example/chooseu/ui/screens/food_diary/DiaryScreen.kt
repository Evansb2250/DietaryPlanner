package com.example.chooseu.ui.screens.food_diary

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.chooseu.common.sidePadding
import com.example.chooseu.core.diary.DiaryScreenViewModel
import com.example.chooseu.core.diary.states.DiaryScreenStates
import com.example.chooseu.core.toolbar_states.ToolBarState
import com.example.chooseu.ui.theme.appColor
import com.example.chooseu.ui.ui_components.mealItem.MealItem
import com.example.chooseu.ui.ui_components.toolbar.ChooseUToolBar

@Preview(
    showBackground = true,
)
@Composable
fun DiaryScreen(
    dateLong: Long? = null,
    viewModel: DiaryScreenViewModel = hiltViewModel(),
) {
    BackHandler {

    }

    LaunchedEffect(
        key1 = dateLong,
        ){
        viewModel.setUpScreenState(dateLong)
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
            val state = viewModel.state.collectAsStateWithLifecycle().value

            when (state) {
                is DiaryScreenStates.FoodDiaryEntry -> {
                    Header(
                        state.date,
                        getPreviousDate = {viewModel.getPreviousDate(state)},
                        getNextDate = {viewModel.getNextDate(state)}
                    )
                    LazyColumn(
                        modifier = Modifier
                            .background(color = appColor)
                            .fillMaxSize()
                            .padding(sidePadding)
                    ) {
                        MealItem(
                            dayOfMonth = state.date,
                            menuItem = state.breakfast,
                            onAddFood = viewModel::addFoodItem,
                        )
                        MealItem(
                            dayOfMonth = state.date,
                            menuItem = state.lunch,
                            onAddFood = viewModel::addFoodItem,
                        )
                        MealItem(
                            dayOfMonth = state.date,
                            menuItem = state.dinner,
                            onAddFood = viewModel::addFoodItem,
                        )
                    }
                }
            }
        }
    }
}
