package com.example.skycast.data.remoteData

import com.example.skycast.data.dataClasses.currentWeather.CurrentWeatherRespond
import com.example.skycast.data.dataClasses.forecastRespond.ForecasteRespond
import com.example.skycast.data.remoteData.retrofit.RetrofitHelper
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import retrofit2.Response

class WearherRemoreDataSourse(val retrofitHelper: RetrofitHelper) {

    suspend fun getCurrentWeather(
        lat: String,
        lon: String,
        language: String,
        unit: String
    ): Flow<CurrentWeatherRespond?> {

            val result = retrofitHelper.apiServices?.getCurrentWeather(
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
        val result=retrofitHelper.apiServices?.getForecast(lat,lon,language= language,unit = unit)?.body()
      return flowOf(result)
    }
}

