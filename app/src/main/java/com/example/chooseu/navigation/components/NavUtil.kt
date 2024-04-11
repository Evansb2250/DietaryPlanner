package com.example.chooseu.navigation.components

import androidx.navigation.NavBackStackEntry
import com.example.chooseu.navigation.components.destinations.destinationArguments.DiaryArgs

fun NavBackStackEntry.getUserId(): String = this.arguments?.getString("userId") ?: ""
fun NavBackStackEntry.getLongDate(): Long? =
    this.arguments?.getString(DiaryArgs.LONG_DATE.name)?.toLongOrNull()

