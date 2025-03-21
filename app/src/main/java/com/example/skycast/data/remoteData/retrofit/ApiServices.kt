package com.example.skycast.data.remoteData.retrofit

import com.example.skycast.data.dataClasses.forecastRespond.ForecasteRespond
import com.example.skycast.data.dataClasses.currentWeather.CurrentWeatherRespond
import com.example.skycast.utils.Constant
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

private const val lat = Constant.Companion.Parameters.LATITUDE
private const val long = Constant.Companion.Parameters.LONGTUDE
private const val langu = Constant.Companion.Parameters.LANGUAGE
private const val unitMeasure = Constant.Companion.Parameters.UNITS
private const val appId = Constant.Companion.Parameters.APP_ID

interface ApiServices {
    @GET(Constant.WEATHER_ENDPOINT)
    suspend fun getCurrentWeather(
        @Query(lat) latitude: String,
        @Query(long) longitude: String,
        @Query(appId) appid: String = Constant.API_KEY,
        @Query(langu) lang: String,
        @Query(unitMeasure) unit: String
    ): Response<CurrentWeatherRespond>

    @GET(Constant.FORECAST_ENDPOINT)
    suspend fun getForecast(
        @Query(lat) latitude: String,
        @Query(long) longitude: String,
        @Query(appId) appid: String = Constant.API_KEY,
        @Query(langu) language: String,
        @Query(unitMeasure) unit: String
    ): Response<ForecasteRespond>


}






