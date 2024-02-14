package com.example.chooseu.utils

sealed class AsyncResponse<T> {
    data class Success<T>(val data: T?): AsyncResponse<T>()
    data class Failed<T>(val data: Any?, val message: String?) : AsyncResponse<T>()
}