package com.example.googlelightcalendar.ui.screens.food_search

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.googlelightcalendar.R
import com.example.googlelightcalendar.common.sidePadding
import com.example.googlelightcalendar.core.diary.searchFood.FoodSearchViewModel
import com.example.googlelightcalendar.core.toolBarStates.ToolBarState
import com.example.googlelightcalendar.ui.ui_components.custom_column.AppColumnContainer
import com.example.googlelightcalendar.ui.ui_components.toolbar.ChooseUToolBar

@Composable
fun FoodSearchScreen(
    title: String,
    vm : FoodSearchViewModel = hiltViewModel()
){
    Scaffold(
        topBar = {
            ChooseUToolBar(
                toolBarState = ToolBarState.Navigated(
                    title = title,
                ),
                navigateBack = vm::navigateBackToFoodDiary,
                navigateToActionDestination = {}
            )
        }
    ) { it ->
        AppColumnContainer {
            OutlinedTextField(
                modifier = Modifier
                    .padding(it)
                    .fillMaxWidth(),
                value = vm.text,
                onValueChange = vm::text::set,
                singleLine = true,
                leadingIcon = {
                    Image(
                        modifier = Modifier
                            .clickable { vm.searchFoodItem(vm.text) }
                            .padding(horizontal = sidePadding),
                        painter = painterResource(
                            id = R.drawable.search_icon,
                        ),
                        contentDescription = "",
                    )
                },
                shape = CircleShape,
                colors = OutlinedTextFieldDefaults.colors(
                    focusedTextColor = Color.White,
                    unfocusedTextColor = Color.White,
                )
            )
        }
    }
}