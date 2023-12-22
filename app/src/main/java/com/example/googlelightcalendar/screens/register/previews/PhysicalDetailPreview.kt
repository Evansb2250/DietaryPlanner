package com.example.googlelightcalendar.screens.register.previews

import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import com.example.googlelightcalendar.core.registration.state.PhysicalDetailState

class PhysicalDetailPreview: PreviewParameterProvider<PhysicalDetailState> {
    override val values: Sequence<PhysicalDetailState>
        get() = sequenceOf(
            PhysicalDetailState.PhysicalDetails()
        )
}
