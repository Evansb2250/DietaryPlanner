package com.example.googlelightcalendar.screens.register.physical

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.googlelightcalendar.R
import com.example.googlelightcalendar.core.registration.Genders
import com.example.googlelightcalendar.core.registration.PhysicalDetailsViewModel
import com.example.googlelightcalendar.core.registration.state.PhysicalDetailState
import com.example.googlelightcalendar.core.registration.state.heightUnits
import com.example.googlelightcalendar.core.registration.state.weightUnits
import com.example.googlelightcalendar.screens.register.previews.PhysicalDetailPreview
import com.example.googlelightcalendar.ui_components.buttons.StandardButton
import com.example.googlelightcalendar.ui_components.calendar.DateSelector
import com.example.googlelightcalendar.ui_components.custom_column.AppColumnContainer
import com.example.googlelightcalendar.ui_components.dialog.ErrorAlertDialog
import com.example.googlelightcalendar.ui_components.menu.CustomDropDownMenu
import com.example.googlelightcalendar.ui_components.text_fields.CustomOutlineTextField
import kotlinx.coroutines.Dispatchers


@Composable
fun PhysicalDetailScreen(
    viewModel: PhysicalDetailsViewModel = hiltViewModel()
) {
    PhysicalDetailContent(
        state = viewModel.state.collectAsState(Dispatchers.Main.immediate).value,
        navToRegisterGoals = viewModel::storePhysicalDetailsInCache,
        retry = viewModel::reset
    )
}




