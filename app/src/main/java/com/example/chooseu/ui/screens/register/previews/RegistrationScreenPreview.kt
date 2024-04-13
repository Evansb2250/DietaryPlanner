package com.example.chooseu.screens.register.previews

import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import com.example.chooseu.ui.screens.register.main.state.InitialRegistrationState


class RegistrationScreenPreview : PreviewParameterProvider<InitialRegistrationState.PersonalInformationState>{
    override val values: Sequence<InitialRegistrationState.PersonalInformationState>
        get() = sequenceOf(
            InitialRegistrationState.PersonalInformationState(),
        )

}