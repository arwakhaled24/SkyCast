package com.example.skycast.data.remoteData

import com.example.skycast.data.dataClasses.currentWeather.CurrentWeatherRespond
import com.example.skycast.data.dataClasses.forecastRespond.ForecasteRespond
import com.example.skycast.data.remoteData.retrofit.RetrofitHelper

class WearherRemoreDataSourse {

    suspend fun getCurrentWeather(
        lat: String,
        lon: String,
        language: String,
        unit: String
    ): CurrentWeatherRespond? {
        try {
            val result = RetrofitHelper.apiServices?.getCurrentWeather(
                latitude = lat,
                longitude = lon,
                lang = language,
                unit = unit
            )
            if (result?.isSuccessful!!) return result.body()
            else  return null

        } catch (e: Exception) {
            return null
        }
    }

    suspend fun getForecast(
        lat: String,
        lon: String,
        language: String,
        unit: String
    ):ForecasteRespond?{
        try {
            val result=RetrofitHelper.apiServices?.getForecast(lat,lon,language= language,unit = unit)
            if (result?.isSuccessful()==true) return result.body()
            else return null
        }catch (e :Exception){
            return null
        }
    }
}

