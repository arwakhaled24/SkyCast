package com.example.skycast.data.repository

import com.example.skycast.data.dataClasses.LocationDataClass
import com.example.skycast.data.dataClasses.currentWeather.CurrentWeatherRespond
import com.example.skycast.data.dataClasses.forecastRespond.ForecasteRespond
import kotlinx.coroutines.flow.Flow

interface IWeatherRepository {
    suspend fun getCurrentWeather(
        lat: String,
        lon: String,
        language: String,
        unit: String
    ): Flow<CurrentWeatherRespond?>

    suspend fun getForecastgetCurrentWeather(
        lat: String,
        lon: String,
        language: String,
        unit: String
    ): Flow<ForecasteRespond?>

    suspend fun getAllFav(): Flow<List<LocationDataClass>>
    suspend fun addFavLocation(locationData: LocationDataClass): Long
    suspend fun deleteFavLocation(locationDataClass: LocationDataClass): Int
}