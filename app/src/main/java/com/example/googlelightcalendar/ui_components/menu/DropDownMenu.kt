package com.example.googlelightcalendar.ui_components.menu

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuBoxScope
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomDropDownMenu(
    selectedOptionText: String,
    modifier: Modifier = Modifier,
    options: List<String>,
    onOptionChange: (String) -> Unit = {}
) {
    var expanded by remember {
        mutableStateOf(false)
    }

    ExposedDropdownMenuBox(
        modifier = modifier
            .background(
                color = Color.White
            )
            .size(
                width = 63.dp,
                height = 56.dp,
            ),
        expanded = expanded,
        onExpandedChange = { expanded = it },


        ) {
        Row(
            modifier = Modifier.fillMaxSize(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = selectedOptionText,
                modifier = Modifier
                    .padding(horizontal = 8.dp)
                    .menuAnchor(),
                textAlign = TextAlign.End
            )

            ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
        }

        dropDown(
            modifier = modifier
                .size(
                    width = 100.dp,
                    height = 56.dp,
                )
                .menuAnchor(),
            expanded = expanded,
            options = options,
            selectedOptionText = { selectedOption ->
                onOptionChange(selectedOption)
            },
            onExpand = { expanded = false }
        )
    }


}

@Composable
@OptIn(ExperimentalMaterial3Api::class)
private fun ExposedDropdownMenuBoxScope.dropDown(
    modifier: Modifier = Modifier,
    expanded: Boolean,
    options: List<String>,
    selectedOptionText: (String) -> Unit,
    onExpand: () -> Unit,
) {
    this.ExposedDropdownMenu(
        modifier = modifier,
        expanded = expanded,
        onDismissRequest = onExpand,
    ) {
        options.forEach { selectedOption ->
            Row(
                modifier = Modifier.fillMaxSize(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Spacer(modifier = Modifier.size(10.dp))
                Text(
                    modifier = Modifier.clickable {
                            selectedOptionText(selectedOption)
                            onExpand()
                        }
                        .padding(ExposedDropdownMenuDefaults.ItemContentPadding),
                    text = selectedOption)
            }
        }
    }
}