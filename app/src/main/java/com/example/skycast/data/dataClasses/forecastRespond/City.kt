package com.example.skycast.data.dataClasses.forecastRespond

data class City(
    val id: Long,
    val name: String,
    val country: String,
    val population: Long,
    val timezone: Long,
    val sunrise: Long,
    val sunset: Long,
)