package com.example.skycast.home.view

import androidx.compose.runtime.Composable


@Composable
fun getIcon(iconCode: String): String {
    return when (iconCode) {
        "01d" -> "☀️"
        "01n" -> "🌑"
        "11d", "11n" -> "⛈"
        "13d", "13n" -> "❄"
        "02d" -> "🌤️"
        "10d" -> "🌦️"
        "03d", "03n" -> "☁️"
        "09d", "09n", "10n" -> "🌧️"
        "04d" -> "☁️"
        "04n" -> "☁︎"
        "50d", "50n" -> "🌫️"
        "02n" -> "☁"
        else -> ""
    }

}