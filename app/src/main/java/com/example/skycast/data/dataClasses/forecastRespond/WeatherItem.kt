package com.example.skycast.data.dataClasses.forecastRespond

import com.example.skycast.data.dataClasses.Clouds
import com.example.skycast.data.dataClasses.Main
import com.example.skycast.data.dataClasses.Sys
import com.example.skycast.data.dataClasses.Weather
import com.example.skycast.data.dataClasses.Wind
import com.google.gson.annotations.SerializedName
import kotlin.collections.List

data class WeatherItem(
    val dt: Long,
    val main: Main,
    val weather: List<Weather>,
    val clouds: Clouds,
    val wind: Wind,
    val visibility: Long,
    val pop: Double,
    val sys: Sys,
    @SerializedName("dt_txt")
    val dtTxt: String,
    val rain: Rain?,
)