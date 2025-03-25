package com.example.skycast.data.remoteData

import com.example.skycast.data.dataClasses.currentWeather.CurrentWeatherRespond
import com.example.skycast.data.dataClasses.forecastRespond.ForecasteRespond
import com.example.skycast.data.remoteData.retrofit.RetrofitHelper
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import retrofit2.Response

class WearherRemoreDataSourse {

    suspend fun getCurrentWeather(
        lat: String,
        lon: String,
        language: String,
        unit: String
    ): Flow<CurrentWeatherRespond?> {

            val result = RetrofitHelper.apiServices?.getCurrentWeather(
                latitude = lat,
                longitude = lon,
                lang = language,
                unit = unit
            )?.body()
        return flowOf(result)
    }

    suspend fun getForecast(
        lat: String,
        lon: String,
        language: String,
        unit: String
    ): Flow<ForecasteRespond?> {
        val result=RetrofitHelper.apiServices?.getForecast(lat,lon,language= language,unit = unit)?.body()
      return flowOf(result)
    }
}

