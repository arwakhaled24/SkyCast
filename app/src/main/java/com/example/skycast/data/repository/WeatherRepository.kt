package com.example.skycast.data.repository

import com.example.skycast.data.LocalData.LocalDataSource
import com.example.skycast.data.dataClasses.LocationDataClass
import com.example.skycast.data.dataClasses.currentWeather.CurrentWeatherRespond
import com.example.skycast.data.dataClasses.forecastRespond.ForecasteRespond
import com.example.skycast.data.remoteData.WearherRemoreDataSourse
import kotlinx.coroutines.flow.Flow

class WeatherRepository(
    private val weatherRemoreDataSourse: WearherRemoreDataSourse,
   private val weatherLocalDataSourse: LocalDataSource
) {
    suspend fun getCurrentWeather(
        lat: String,
        lon: String,
        language: String,
        unit: String
    ): Flow<CurrentWeatherRespond?> {
        return weatherRemoreDataSourse.getCurrentWeather(lat,lon,language,unit)
    }

    suspend fun getForecastgetCurrentWeather(
        lat: String,
        lon: String,
        language: String,
        unit: String
    ): Flow<ForecasteRespond?> {
        return weatherRemoreDataSourse.getForecast(lat,lon,language,unit)
    }

    suspend fun getAllFav():Flow<List<LocationDataClass>>{
      return  weatherLocalDataSourse.getAllFavLocation()
    }


    suspend fun addFavLocation(locationData:LocationDataClass) :Long{
        return weatherLocalDataSourse.addFavLocation(locationData)
    }

    suspend fun deleteFavLocation(locationDataClass: LocationDataClass):Int{
        return weatherLocalDataSourse.deleteFavLocation(locationDataClass)
    }






}