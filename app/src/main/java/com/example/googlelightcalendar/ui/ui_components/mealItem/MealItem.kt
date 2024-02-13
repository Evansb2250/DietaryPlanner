package com.example.googlelightcalendar.ui.ui_components.mealItem

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Divider
import androidx.compose.material3.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import androidx.compose.ui.unit.dp
import com.example.googlelightcalendar.core.diary.models.FoodDiaryItem
import com.example.googlelightcalendar.ui.theme.appColor
import com.example.googlelightcalendar.ui.theme.yellowMain

fun LazyListScope.MealItem(
    dayOfMonth: String,
    menuItem: FoodDiaryItem,
    color: Color = Color.White,
    onAddFood: (
        day: String,
        mealType: String,
    ) -> Unit = { _, _ -> },
) {
    item {
        Row(
            modifier = Modifier.background(
                color = appColor
            )
        ) {
            Text(
                text = menuItem.typeOfMeal.entryType,
                color = color,
                style = mealHeader
            )
            Text(
                modifier = Modifier.fillMaxWidth(),
                text = "${menuItem.calorieCount}",
                color = color,
                textAlign = TextAlign.Right,
                style = mealHeader,
            )
        }
        Divider()
    }

    this.items(
        items = menuItem.items,
        key = {
            it
        },
    ) {
        Row(
            modifier = Modifier
                .padding(
                    top = 3.dp
                )
                .background(
                    color = appColor
                )
        ) {
            Box(
                contentAlignment = Alignment.TopStart,
            ) {
                Text(
                    modifier = Modifier.padding(
                        top = 3.dp
                    ),
                    text = it.name,
                    color = color,
                    style = foodStyle
                )
            }
            Box(
                contentAlignment = Alignment.TopEnd,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    modifier = Modifier.padding(
                        top = 3.dp
                    ),
                    text = it.calorieCount,
                    color = color,
                    style = foodStyle
                )
            }
        }
        Text(
            modifier = Modifier
                .padding(
                    top = 3.dp,
                    bottom = 7.dp
                )
                .fillMaxWidth(),
            text = it.generalNutrientVal,
            textAlign = TextAlign.Start,
            color = color
        )
        Divider()
    }

    item {
        Box(
            modifier = Modifier
                .fillMaxWidth(),
            contentAlignment = Alignment.TopEnd,
        ) {
            Text(
                modifier = Modifier.clickable {
                    onAddFood(
                        dayOfMonth,
                        menuItem.typeOfMeal.entryType
                    )
                },
                text = "Add Food", color = yellowMain
            )
            Spacer(modifier = Modifier.size(60.dp))
        }
    }
}

val mealHeader = TextStyle(
    fontSize = TextUnit(
        value = 5f,
        type = TextUnitType.Em,
    )
)

val foodStyle = TextStyle(
    fontSize = TextUnit(
        value = 4f,
        type = TextUnitType.Em,
    )
)