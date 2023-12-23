package com.example.googlelightcalendar.screens.register.previews

import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import com.example.googlelightcalendar.core.registration.InitialRegistrationState


class RegistrationScreenPreview : PreviewParameterProvider<InitialRegistrationState.PersonalInformationState>{
    override val values: Sequence<InitialRegistrationState.PersonalInformationState>
        get() = sequenceOf(
            InitialRegistrationState.PersonalInformationState(),
        )

}