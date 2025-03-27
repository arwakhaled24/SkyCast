package com.example.skycast.data.dataClasses.forecastRespond

data class ForecasteRespond(
    val cod: String,
    val message: Long,
    val cnt: Long,
    val list: List<WeatherItem>,
    val city: City,
) {

}