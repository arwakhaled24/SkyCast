package com.example.skycast.data.repository

import com.example.skycast.data.dataClasses.LocationDataClass
import com.example.skycast.data.dataClasses.currentWeather.CurrentWeatherRespond
import com.example.skycast.data.dataClasses.forecastRespond.ForecasteRespond
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class Fakerepository:IWeatherRepository {

    private val fakeLocations = mutableListOf(
        LocationDataClass(longitude = "12", latitude = "14", CityName = "arwa", id = 1),
        LocationDataClass(longitude = "34", latitude = "56", CityName = "arwa", id = 2)
    )


    override suspend fun getCurrentWeather(
        lat: String,
        lon: String,
        language: String,
        unit: String
    ): Flow<CurrentWeatherRespond?> {
        TODO("Not yet implemented")
    }

    override suspend fun getForecastgetCurrentWeather(
        lat: String,
        lon: String,
        language: String,
        unit: String
    ): Flow<ForecasteRespond?> {
        TODO("Not yet implemented")
    }

    override suspend fun getAllFav(): Flow<List<LocationDataClass>> {
        return flow { emit(fakeLocations) }
    }

    override suspend fun addFavLocation(locationData: LocationDataClass): Long {
        fakeLocations.add(locationData)
        return locationData.id.toLong()
    }

    override suspend fun deleteFavLocation(locationDataClass: LocationDataClass): Int {
        TODO("Not yet implemented")
    }
}