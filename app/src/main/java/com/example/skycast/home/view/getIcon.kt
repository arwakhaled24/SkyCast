package com.example.skycast.home.view

import androidx.compose.runtime.Composable


@Composable
fun getIcon(iconCode: String): String {
    if (iconCode == "01d") return "☀️"
    else if (iconCode == "01n") return "🌑"
    else if (iconCode == "11d" || iconCode == "11n") return "⛈"
    else if (iconCode == "13d" || iconCode == "13n") return "❄"
    else if (iconCode == "02d") return "🌤️"
    else if (iconCode == "10d") return "🌦️"
    else if (iconCode == "03d" || iconCode == "03n") return "☁️"
    else if (iconCode == "09d" || iconCode == "09n" || iconCode == "10n") return "🌧️"
    else if (iconCode == "04d") return "☁️"
    else if (iconCode == "04n") return "☁︎"
    else if (iconCode == "50d" || iconCode == "50n") return "🌫️"
    else if (iconCode == "02n") return "☁"
    return ""

}