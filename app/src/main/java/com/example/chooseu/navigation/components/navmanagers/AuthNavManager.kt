package com.example.chooseu.navigation.components.navmanagers

import kotlinx.coroutines.CoroutineScope
import javax.inject.Inject

class AuthNavManager @Inject constructor(
    externalScope: CoroutineScope,
) : NavigationManger(externalScope)
