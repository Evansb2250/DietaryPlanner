package com.example.chooseu.screens.register.previews

import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import com.example.chooseu.core.registration.state.PhysicalDetailState

class PhysicalDetailPreview: PreviewParameterProvider<PhysicalDetailState> {
    override val values: Sequence<PhysicalDetailState>
        get() = sequenceOf(
            PhysicalDetailState.PhysicalDetails()
        )
}
