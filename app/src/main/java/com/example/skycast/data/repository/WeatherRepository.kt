package com.example.skycast.data.repository

import com.example.skycast.data.LocalData.WeatherLocalDataSourse
import com.example.skycast.data.dataClasses.currentWeather.CurrentWeatherRespond
import com.example.skycast.data.dataClasses.forecastRespond.ForecasteRespond
import com.example.skycast.data.remoteData.WearherRemoreDataSourse

class WeatherRepository(
    private val weatherRemoreDataSourse: WearherRemoreDataSourse,
   private val weatherLocalDataSourse: WeatherLocalDataSourse
) {

    suspend fun getCurrentWeather(
        lat: String,
        lon: String,
        language: String,
        unit: String
    ): CurrentWeatherRespond? {
        return weatherRemoreDataSourse.getCurrentWeather(lat,lon,language,unit)
    }

    suspend fun getForecastgetCurrentWeather(
        lat: String,
        lon: String,
        language: String,
        unit: String
    ):ForecasteRespond?{
        return weatherRemoreDataSourse.getForecast(lat,lon,language,unit)
    }



}