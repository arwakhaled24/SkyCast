package com.example.skycast.home.view

import android.icu.text.SimpleDateFormat
import com.example.skycast.data.dataClasses.forecastRespond.WeatherItem
import java.util.Date
import java.util.Locale

fun getCurrentTime(): String {
    val sdf = SimpleDateFormat("h:mm a", Locale.getDefault())
    return sdf.format(Date(System.currentTimeMillis()))
}

fun getTime(time: Long): String {
    val sdf = SimpleDateFormat("h", Locale.getDefault())
    return sdf.format(Date(time))
}

fun getTimeWithM(time: Long): String {
    val sdf = SimpleDateFormat("h:mm a", Locale.getDefault())
    return sdf.format(Date(time))
}

fun toDaysForecast(wearherItem: List<WeatherItem>): List<WeatherItem> {
    val todayForecast = wearherItem.subList(0, 9).toList()
    return todayForecast
}

fun interpolateHourlyForecast(weatherItems: List<WeatherItem>): List<WeatherItem> {
    val interpolatedForecast = mutableListOf<WeatherItem>()

    for (i in 0 until weatherItems.size - 1) {
        val currentItem = weatherItems[i]
        val nextItem = weatherItems[i + 1]

        interpolatedForecast.add(currentItem)


        val tempDiff = (nextItem.main.temp - currentItem.main.temp) / 3
        val pressureDiff = (nextItem.main.pressure - currentItem.main.pressure) / 3
        val humidityDiff = (nextItem.main.humidity - currentItem.main.humidity) / 3


        repeat(2) { j ->
            val interpolatedItem = currentItem.copy(
                dt = currentItem.dt + (j + 1) * 3600,
                dtTxt = "",
                main = currentItem.main.copy(
                    temp = currentItem.main.temp + (j + 1) * tempDiff,
                    pressure = currentItem.main.pressure + (j + 1) * pressureDiff,
                    humidity = currentItem.main.humidity + (j + 1) * humidityDiff.toInt()
                )
            )
            interpolatedForecast.add(interpolatedItem)
        }
    }
    interpolatedForecast.add(weatherItems.last())

    return interpolatedForecast
}