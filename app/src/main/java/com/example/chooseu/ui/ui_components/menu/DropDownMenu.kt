package com.example.chooseu.ui.ui_components.menu

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Divider
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.chooseu.ui.theme.Gray500


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomDropDownMenu(
    modifier: Modifier = Modifier,
    selectedOptionText: String,
    options: List<String>,
    enable: Boolean = true,
    onOptionChange: (String) -> Unit = {},
    onIndexChange: (Int) -> Unit = {},
) {
    var expanded by remember {
        mutableStateOf(false)
    }

    var itemHeight by remember {
        mutableStateOf(0.dp)
    }

    var itemWidth by remember {
        mutableStateOf(0.dp)
    }

    val density = LocalDensity.current

    Box(
        modifier = modifier
            .background(
                color = if (enable) Color.White else Gray500
            )
            .clickable {
                if (enable) {
                    expanded = !expanded
                }
            }
            .onSizeChanged {
                itemWidth = with(density) { it.width.toDp() }
                itemHeight = with(density) { it.height.toDp() }
            },
        contentAlignment = Alignment.Center,
    ) {
        Row(
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                modifier = Modifier,
                text = selectedOptionText,
                textAlign = TextAlign.End
            )
            ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
        }
        if(enable){
            DropdownMenu(
                modifier = Modifier
                    .width(itemWidth)
                    .height(itemHeight * options.size)
                    .background(
                        color = Color.White,
                    ),
                expanded = expanded,
                onDismissRequest = {
                    expanded = false
                }
            ) {
                options.forEachIndexed { index, selectedOption->
                    DropdownMenuItem(
                        modifier = Modifier
                            .width(itemWidth)
                            .background(
                                color = Color.White,
                            ),
                        text = {
                            Text(
                                modifier = Modifier.fillMaxWidth(),
                                text = selectedOption,
                                textAlign = TextAlign.Center,
                            )
                        },
                        onClick = {
                            onOptionChange(selectedOption)
                            onIndexChange(index)
                            expanded = false
                        }
                    )
                    Divider()
                }
            }
        }
    }
}
