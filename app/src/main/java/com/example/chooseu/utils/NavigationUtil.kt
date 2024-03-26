package com.example.chooseu.utils

import android.util.Log
import com.example.chooseu.navigation.components.Navigation


fun buildDestination(
    navigation: Navigation,
    parameters: Map<String, String>,
): Navigation {
    //Makes the path mutable
    var destination = navigation.destination

    //checks the list of paramters
    parameters.forEach { (key, value) ->
        destination = destination.replace("{$key}", value)
        Log.d("Builider", "original $value    after $destination")
    }
    return object : Navigation {
        override val destination: String
            get() = destination
        override val arguments: List<String>
            get() = parameters.map { it.value }

    }
}