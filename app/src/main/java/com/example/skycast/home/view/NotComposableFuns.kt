package com.example.skycast.home.view

import android.content.Context
import android.icu.text.SimpleDateFormat
import com.example.skycast.R
import com.example.skycast.data.dataClasses.forecastRespond.WeatherItem
import com.example.skycast.utils.Constant
import com.example.skycast.utils.SharedPreferences
import java.util.Date
import java.util.Locale

fun getTime(time: Long): String {
    val sdf = SimpleDateFormat("h", Locale.getDefault())
   return sdf.format(Date(time * 1000))

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

fun getTemperatureSymbol(context: Context): String {
    return when ( SharedPreferences.getInstance(context).getTemperature()) {
        Constant.Companion.sharedPrefrances(context).CELSIUS-> context.getString(R.string.celsius_c)
        Constant.Companion.sharedPrefrances(context).FAHRENHEIT -> context.getString(R.string.fahrenheit_f)
        Constant.Companion.sharedPrefrances(context).KELVIN -> context.getString(R.string.kelvin_k)
        else -> context.getString(R.string.celsius_c)
    }
}