package com.example.skycast.data.remoteData

import com.example.skycast.data.dataClasses.currentWeather.CurrentWeatherRespond
import com.example.skycast.data.dataClasses.forecastRespond.ForecasteRespond
import kotlinx.coroutines.flow.Flow

class RemoteDataFack :IWearherRemoreDataSourse{


    override suspend fun getCurrentWeather(
        lat: String,
        lon: String,
        language: String,
        unit: String
    ): Flow<CurrentWeatherRespond?> {
        TODO("Not yet implemented")
    }

    override suspend fun getForecast(
        lat: String,
        lon: String,
        language: String,
        unit: String
    ): Flow<ForecasteRespond?> {
        TODO("Not yet implemented")
    }
}