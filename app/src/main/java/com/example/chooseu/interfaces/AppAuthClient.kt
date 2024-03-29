package com.example.chooseu.interfaces

import android.content.Intent
import androidx.activity.result.ActivityResultLauncher

interface AppAuthClient {
    fun registerAuthLauncher(
        launcher: ActivityResultLauncher<Intent>,
    )

    fun handleAuthorizationResponse(
        intent: Intent,
    )
}