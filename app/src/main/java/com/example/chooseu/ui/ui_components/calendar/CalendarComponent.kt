package com.example.chooseu.ui.ui_components.calendar

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.chooseu.R
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.TimeZone

/**
 *  the Date Picker and the DatePickerDialog
 *
 *  he Date Picker composable is designed to display a full-screen view of the DatePicker. It offers a range
 *  of features, including date validation, which allows you to disable future dates or implement custom logic based on your requirements.
 *
 *  To enable date validation, you’ll need to provide your own implementation of the SelectableDates interface.
 */

private fun convertMillisToDate(millis: Long): String {
    val dateFormat = SimpleDateFormat("MM/dd/yyyy", Locale.US)
    dateFormat.timeZone = TimeZone.getTimeZone("UTC") // Set the time zone to UTC
    return dateFormat.format(Date(millis))
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DateSelector(
    onDateSelected: (String) -> Unit,
    onDismiss: () -> Unit
) {
    val datePickerState = rememberDatePickerState()

    val selectedDate = datePickerState.selectedDateMillis?.let {
        convertMillisToDate(it)
    } ?: ""

    DatePickerDialog(
        onDismissRequest = { onDismiss() },
        confirmButton = {
            Button(onClick = {
                onDateSelected(selectedDate)
                onDismiss()
            }

            ) {
                Text(text = "OK")
            }
        },
        dismissButton = {
            Button(
                onClick = {
                    onDismiss()
                },
            ) {
                Text(text = "Cancel")
            }
        }
    ) {
        DatePicker(
            state = datePickerState
        )
    }
}

@Composable
fun DateSelector(
    initialDate: MutableState<String>,
    modifier: Modifier,
    onDateChange: (String) -> Unit = {},
) {
    var date = initialDate.value

    var showDatePicker by remember {
        mutableStateOf(false)
    }

    Box(
        modifier = modifier
            .size(
                width = 320.dp,
                height = 56.dp,
            )
            .background(
                color = Color.White,
                shape = RoundedCornerShape(4.dp),
            )
            .clickable(
                enabled = true,
                onClickLabel = null,
                onClick = { showDatePicker = true },
            )
            .fillMaxWidth(),
        contentAlignment = Alignment.Center
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Text(
                text = date.ifEmpty { "Enter here.." }
            )
        }
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.End
        ) {

            Image(
                painter = painterResource(id = R.drawable.calendar),
                contentDescription = "calendar"
            )
        }
    }

    if (showDatePicker) {
        DateSelector(
            onDateSelected = { newDate ->
                date = newDate
                onDateChange(date)
            },
            onDismiss = { showDatePicker = false }
        )
    }
}