package com.example.skycast.data.LocalData

import com.example.skycast.data.LocalData.room.Dao
import com.example.skycast.data.dataClasses.LocationDataClass
import kotlinx.coroutines.flow.Flow

class LocalDataSource(val dao: Dao) : ILocalDataSource {

    override suspend fun getAllFavLocation(): Flow<List<LocationDataClass>> {
        return dao.getAll()
    }

    override suspend fun deleteFavLocation(locationDataClass: LocationDataClass):Int{
        return dao.deleteFavLocation(locationDataClass)
    }

    override suspend fun addFavLocation(locationDataClass: LocationDataClass):Long{
        return dao.insertFavLocation(locationDataClass)
    }

}