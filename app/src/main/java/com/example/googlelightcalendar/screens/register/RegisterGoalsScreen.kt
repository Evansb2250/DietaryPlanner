package com.example.googlelightcalendar.screens.register

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.googlelightcalendar.R
import com.example.googlelightcalendar.ui_components.buttons.StandardButton
import com.example.googlelightcalendar.ui_components.custom_column.AppColumnContainer


@Preview(showBackground = true)
@Composable
fun RegisterGoalsScreen() {
    var scrollState = rememberScrollState()
    AppColumnContainer(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceAround,
    ) {
        Image(
            modifier = Modifier
                .fillMaxWidth()
                .align(
                    alignment = Alignment.CenterHorizontally,
                ),
            painter = painterResource(
                id = R.drawable.main_logo,
            ),
            contentDescription = "Logo"
        )

        Spacer(modifier = Modifier.size(30.dp))

        Text(
            modifier = Modifier
                .fillMaxWidth(),
            text = "What do you want to achieve?",
            color = Color.White,
            textAlign = TextAlign.Left,
        )

        Spacer(
            modifier = Modifier.size(30.dp),
        )

        Column(
            modifier = Modifier
                .weight(10f)
                .verticalScroll(scrollState)
                .padding(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceAround,
        ) {

            Column {


                AsyncImage(
                    modifier = Modifier.size(
                        width = 318.dp,
                        height = 249.dp
                    ),
                    model = "https://img.freepik.com/free-photo/salad-from-tomatoes-cucumber-red-onions-lettuce-leaves-healthy-summer-vitamin-menu-vegan-vegetable-food-vegetarian-dinner-table-top-view-flat-lay_2829-6482.jpg?w=1380&t=st=1702956348~exp=1702956948~hmac=9b12dccffd3c5394d1ac3f8a4c1e72eb80b319b5dd26441b291e4a593e90ee2b",
                    contentDescription = ""
                )

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Start,
                ) {
                    RadioButton(
                        selected = false,
                        onClick = { /*TODO*/ },
                    )
                    Text(
                        text = "Loose Weight",
                        color = Color.White,
                    )
                }
            }

            Spacer(modifier = Modifier.size(30.dp))

            Column {
                AsyncImage(
                    modifier = Modifier.size(
                        width = 318.dp,
                        height = 249.dp
                    ),
                    model = "https://img.freepik.com/premium-photo/table-full-food-including-burgers-fries-other-foods_873925-7494.jpg?w=1380",
                    contentDescription = ""
                )

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Start,
                ) {
                    RadioButton(
                        selected = false,
                        onClick = { /*TODO*/ },
                    )
                    Text(
                        text = "Gain Weight",
                        color = Color.White,
                    )
                }

            }


            Spacer(
                modifier = Modifier.size(30.dp),
            )

            Column {

                AsyncImage(
                    modifier = Modifier.size(
                        width = 318.dp,
                        height = 249.dp
                    ),
                    model = "https://img.freepik.com/free-photo/flay-lay-scale-weights_23-2148262188.jpg?w=1380&t=st=1702956312~exp=1702956912~hmac=bc1753ce093a63f1aa8e695185b62b6729c8216cd81d277c9a1bd44b9d1d445f",
                    contentDescription = ""
                )


                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Start,
                ) {
                    RadioButton(selected = false, onClick = { /*TODO*/ })
                    Text(
                        text = "Maintain Weight",
                        color = Color.White,
                    )
                }

            }
            Spacer(modifier = Modifier.size(30.dp))
        }

        Spacer(modifier = Modifier.size(30.dp))

        Spacer(modifier = Modifier.size(30.dp))

        StandardButton(
            modifier = Modifier.fillMaxWidth(),
            onClick = { /*TODO*/ },
            text = "Click to confirm"
        )

    }

}



