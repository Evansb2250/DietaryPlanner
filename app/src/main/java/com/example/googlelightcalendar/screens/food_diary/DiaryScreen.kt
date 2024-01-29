package com.example.googlelightcalendar.screens.food_diary

import android.view.MotionEvent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Divider
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInteropFilter
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import androidx.compose.ui.unit.dp
import com.example.googlelightcalendar.R
import com.example.googlelightcalendar.core.toolBarStates.ToolBarState
import com.example.googlelightcalendar.screens.loginScreen.sidePadding
import com.example.googlelightcalendar.ui.theme.appColor
import com.example.googlelightcalendar.ui.theme.yellowMain
import com.example.googlelightcalendar.ui_components.toolbar.ChooseUToolBar

@Preview(
    showBackground = true,
)
@Composable
fun DiaryScreen() {
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            ChooseUToolBar(
                toolBarState = ToolBarState.Home,
                navigateBack = {},
                navigateToActionDestination = {},
            )
        }
    ) { it ->
        Column(
            modifier = Modifier.padding(it)
        ) {

            Header()
            LazyColumn(
                modifier = Modifier
                    .background(color = appColor)
                    .padding(sidePadding)
            ) {
                MealItem(
                    title = "Breakfast",
                )

                MealItem(
                    title = "Lunch",
                )

                MealItem(
                    title = "Dinner",
                )

                MealItem(
                    title = "Snacks",
                )
            }
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

fun LazyListScope.MealItem(
    title: String,
    color: Color = Color.White
) {
    item {
        Row(
            modifier = Modifier.background(
                color = appColor
            )
        ) {
            Text(
                text = title,
                color = color,
                style = mealHeader
            )
            Text(
                modifier = Modifier.fillMaxWidth(),
                text = "748",
                color = color,
                textAlign = TextAlign.Right,
                style = mealHeader,
            )
        }
        Divider()
    }

    items(dummySample) {
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
            modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.TopEnd
        ) {
            Text(
                text = "Add Food", color = yellowMain
            )
            Spacer(modifier = Modifier.size(60.dp))
        }
    }
}

data class FoodItem(
    val name: String,
    val calorieCount: String,
    val generalNutrientVal: String,
)

val dummySample = listOf(
    FoodItem(
        name = "Chicken", calorieCount = "231", generalNutrientVal = "164 cal, Protein 31g"
    ),
    FoodItem(
        name = "rice", calorieCount = "231", generalNutrientVal = "164 cal, Protein 31g"
    ),
    FoodItem(
        name = "Brocoli", calorieCount = "239", generalNutrientVal = "164 cal, Protein 31g"
    ),
    FoodItem(
        name = "Beans", calorieCount = "231", generalNutrientVal = "164 cal, Protein 31g"
    ),
)


@OptIn(ExperimentalComposeUiApi::class)
@Preview(
    showBackground = true,
)
@Composable
fun Header() {

    val density = LocalDensity.current
    val height = remember {
        mutableStateOf(5.dp)
    }

    var buttonBackground by remember {
        mutableStateOf(appColor)
    }


    Row(
        modifier = Modifier
            .background(
                color = appColor,
            )
            .onSizeChanged {
                height.value = with(density) {
                    it.height.toDp()
                }
            }
            .fillMaxWidth(),
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            contentAlignment = Alignment.CenterStart,
        ) {
            IconButton(
                onClick = { /*TODO*/ }) {
                Image(
                    painter = painterResource(
                        id = R.drawable.left_outline_arrow
                    ),
                    contentDescription = ""
                )
            }
        }
        Box(
            modifier = Modifier
                .height(height = height.value)
                .weight(1f),
            contentAlignment = Alignment.Center,
        ) {
            Text(
                modifier = Modifier
                    .fillMaxWidth(),
                color = yellowMain,
                text = "Today",
                textAlign = TextAlign.Center,
            )
        }
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            contentAlignment = Alignment.CenterEnd,
        ) {
            IconButton(
                modifier = Modifier.pointerInteropFilter {
                    when (it.action){
                        MotionEvent.ACTION_DOWN -> {
                            buttonBackground = Color.Gray
                        }
                        MotionEvent.ACTION_UP -> {
                            buttonBackground = appColor
                        }
                    }
                    true
                },
                onClick = { /*TODO*/ }) {
                Image(
                    modifier = Modifier.background(
                        color = buttonBackground,
                        shape = CircleShape
                    ),
                    painter = painterResource(
                        id = R.drawable.right_outline_arrow
                    ),
                    contentDescription = ""
                )

            }
        }
    }
}