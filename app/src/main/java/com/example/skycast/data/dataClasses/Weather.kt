package com.example.skycast.data.dataClasses

data class Weather(
    val id: Long,
    val main: String,
    val description: String,
    val icon: String,
)