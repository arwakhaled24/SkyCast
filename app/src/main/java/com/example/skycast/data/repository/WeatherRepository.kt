package com.example.skycast.data.repository

import com.example.skycast.data.LocalData.ILocalDataSource
import com.example.skycast.data.LocalData.LocalDataSource
import com.example.skycast.data.dataClasses.LocationDataClass
import com.example.skycast.data.dataClasses.currentWeather.CurrentWeatherRespond
import com.example.skycast.data.dataClasses.forecastRespond.ForecasteRespond
import com.example.skycast.data.remoteData.IWearherRemoreDataSourse
import com.example.skycast.data.remoteData.WearherRemoreDataSourse
import kotlinx.coroutines.flow.Flow

class WeatherRepository private constructor(
    private val wearherRemoreDataSourse: IWearherRemoreDataSourse,
    private val weatherLocalDataSource: ILocalDataSource
) : IWeatherRepository {
    override suspend fun getCurrentWeather(
        lat: String,
        lon: String,
        language: String,
        unit: String
    ): Flow<CurrentWeatherRespond?> {
        return wearherRemoreDataSourse.getCurrentWeather(lat,lon,language,unit)
    }

    override suspend fun getForecastgetCurrentWeather(
        lat: String,
        lon: String,
        language: String,
        unit: String
    ): Flow<ForecasteRespond?> {
        return wearherRemoreDataSourse.getForecast(lat,lon,language,unit)
    }

    override suspend fun getAllFav():Flow<List<LocationDataClass>>{
      return  weatherLocalDataSource.getAllFavLocation()
    }


    override suspend fun addFavLocation(locationData:LocationDataClass) :Long{
        return weatherLocalDataSource.addFavLocation(locationData)
    }

    override suspend fun deleteFavLocation(locationDataClass: LocationDataClass):Int{
        return weatherLocalDataSource.deleteFavLocation(locationDataClass)
    }

    companion object{
        private var INSTANCE : WeatherRepository? = null
        fun getInstance(remoteDataSource: IWearherRemoreDataSourse ,
                        localDataSource: ILocalDataSource): WeatherRepository {
            return INSTANCE ?: synchronized(this){
                val temp = WeatherRepository(remoteDataSource, localDataSource)
                INSTANCE = temp
                temp
            }
        }
    }





}