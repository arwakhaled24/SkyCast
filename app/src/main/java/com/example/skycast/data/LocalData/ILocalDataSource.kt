package com.example.skycast.data.LocalData

import com.example.skycast.data.dataClasses.LocationDataClass
import kotlinx.coroutines.flow.Flow

interface ILocalDataSource {
    suspend fun getAllFavLocation(): Flow<List<LocationDataClass>>

    suspend fun deleteFavLocation(locationDataClass: LocationDataClass): Int
    suspend fun addFavLocation(locationDataClass: LocationDataClass): Long
}