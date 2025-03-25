package com.example.skycast.data


sealed class RespondStatus<out T> {
    data object Loading : RespondStatus<Nothing>()
    data class Success<T>(val result: T) : RespondStatus<T>()
    data class Error(val error: Throwable) : RespondStatus<Nothing>()
}
