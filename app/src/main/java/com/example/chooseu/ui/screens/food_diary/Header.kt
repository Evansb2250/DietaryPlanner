package com.example.chooseu.ui.screens.food_diary

import android.view.MotionEvent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.chooseu.R
import com.example.chooseu.ui.theme.appColor
import com.example.chooseu.ui.theme.yellowMain


@OptIn(ExperimentalComposeUiApi::class)
@Preview(
    showBackground = true,
)
@Composable
fun Header(
    date: String = "",
    getPreviousDate: () -> Unit = {},
    getNextDate: () -> Unit = {},
) {

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
                onClick = getPreviousDate,
            ) {
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
                text = date,
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
//                modifier = Modifier.pointerInteropFilter {
//                    when (it.action) {
//                        MotionEvent.ACTION_DOWN -> {
//                            buttonBackground = Color.Gray
//                        }
//
//                        MotionEvent.ACTION_UP -> {
//                            buttonBackground = appColor
//                        }
//                    }
//                    true
//                },
                onClick = getNextDate,
            ) {
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