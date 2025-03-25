package com.example.skycast.data.LocalData

import com.example.skycast.data.LocalData.room.Dao
import com.example.skycast.data.dataClasses.LocationDataClass
import kotlinx.coroutines.flow.Flow

class LocalDataSource(val dao: Dao) {

    suspend fun getAllFavLocation(): Flow<List<LocationDataClass>> {
        return dao.getAll()
    }

    suspend fun deleteFavLocation(locationDataClass: LocationDataClass):Int{
        return dao.deleteFavLocation(locationDataClass)
    }

    suspend fun addFavLocation(locationDataClass: LocationDataClass):Long{
        return dao.insertFavLocation(locationDataClass)
    }

}