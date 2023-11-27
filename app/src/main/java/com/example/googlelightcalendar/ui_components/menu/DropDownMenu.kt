package com.example.googlelightcalendar.ui_components.menu

import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuBoxScope
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomDropDownMenu(
    modifier: Modifier = Modifier,
    options: List<String>,
) {
    var expanded by remember {
        mutableStateOf(false)
    }

    var selectedOptionText by remember { mutableStateOf(if (options.isNotEmpty()) options[0] else "") }

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { it -> expanded = it },
    ) {
        TextField(
            modifier = modifier.menuAnchor(),
            value = selectedOptionText,
            enabled = false,
            readOnly = true,
            onValueChange = { },
            label = { Text(text = "Label") },
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
            colors = ExposedDropdownMenuDefaults.textFieldColors(),
        )

        dropDown(
            expanded = expanded,
            options = options,
            selectedOptionText = { select -> selectedOptionText = select},
            onExpand = { expanded = false}
        )
    }
}

@Composable
@OptIn(ExperimentalMaterial3Api::class)
private fun ExposedDropdownMenuBoxScope.dropDown(
    expanded: Boolean,
    options: List<String>,
    selectedOptionText: (String) -> Unit,
    onExpand: () -> Unit,

    ){
    this.ExposedDropdownMenu(
        expanded = expanded,
        onDismissRequest = onExpand,
    ) {
        options.forEach { selectedOption ->
            DropdownMenuItem(
                text = { Text(selectedOption) },
                onClick = {
                    selectedOptionText(selectedOption)
                    onExpand()
                },
                contentPadding = ExposedDropdownMenuDefaults.ItemContentPadding,
            )
        }
    }
}