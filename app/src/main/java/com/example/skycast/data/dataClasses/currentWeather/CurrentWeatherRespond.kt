package com.example.skycast.data.dataClasses.currentWeather

import com.example.skycast.data.dataClasses.Clouds
import com.example.skycast.data.dataClasses.Main
import com.example.skycast.data.dataClasses.Sys
import com.example.skycast.data.dataClasses.Weather
import com.example.skycast.data.dataClasses.Wind

data class CurrentWeatherRespond(
    val weather: List<Weather>,
    val base: String,
    val main: Main,
    val visibility: Long,
    val wind: Wind,
    val clouds: Clouds,
    val dt: Long,
    val sys: Sys,
    val timezone: Long,
    val id: Long,
    val name: String,
    val cod: Long,
)
