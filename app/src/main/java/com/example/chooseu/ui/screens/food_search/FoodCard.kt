package com.example.chooseu.ui.screens.food_search

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.chooseu.R
import com.example.chooseu.ui.screens.food_search.states.FoodItemListActions
import com.example.chooseu.ui.screens.nutrition_screen.FoodItem

@Composable
fun FoodCard(
    foodItem: FoodItem,
    viewNutrientDetails: (foodId: String) -> Unit = {},
    addItem: (FoodItemListActions, foodID: String) -> Unit = { _, _ -> },
) {
    val scrollState = rememberScrollState()
    Card(
        modifier = Modifier.clickable { viewNutrientDetails(foodItem.foodId) }
    ) {
        Row(
            modifier = Modifier
                .height(90.dp)
                .fillMaxWidth()
                .padding(vertical = 10.dp)
        ) {
            AsyncImage(
                modifier = Modifier
                    .weight(1f)
                    .padding(
                        start = 10.dp,
                    ),
                model = foodItem.image,
                contentDescription = "",
            )
            Column(
                modifier = Modifier
                    .weight(2f)
                    .clickable { viewNutrientDetails(foodItem.foodId) }
                    .verticalScroll(scrollState),
            ) {
                Text(
                    text = foodItem.label,
                    fontWeight = FontWeight.Bold,
                )
                Text(
                    text = "Protein ${foodItem.nutrients.protein}"
                )
                Text(
                    text = "Fat ${foodItem.nutrients.fat}"
                )

                Text(
                    text = "Carbs ${foodItem.nutrients.carbohydrates}"
                )

                Text(
                    text = "Fiber ${foodItem.nutrients.fiber}"
                )

                Text(
                    text = "Energy ${foodItem.nutrients.energy}"
                )

            }

            IconButton(
                modifier = Modifier
                    .background(
                        shape = CircleShape,
                        color = Color.Transparent,
                    )
                    .weight(1f),
                onClick = { addItem(FoodItemListActions.INCREMENT, foodItem.foodId) },
            ) {
                Image(
                    painterResource(id = R.drawable.add_icon),
                    contentDescription = null
                )
            }

            Log.d("MapManipulation", "label ${foodItem.label} number ${foodItem.quantity}   ")
            if (foodItem.quantity > 0) {
                Text(text = "${foodItem.quantity}")

                IconButton(
                    modifier = Modifier
                        .background(
                            shape = CircleShape,
                            color = Color.Transparent,
                        )
                        .weight(1f),
                    onClick = { addItem(FoodItemListActions.DECREMENT, foodItem.foodId) },
                ) {
                    Image(
                        painterResource(id = R.drawable.minus_sign),
                        contentDescription = null
                    )
                }
            }

            Spacer(modifier = Modifier.size(5.dp))
        }
    }
}