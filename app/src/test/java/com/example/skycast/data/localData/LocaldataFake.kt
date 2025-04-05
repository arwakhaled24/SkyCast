package com.example.skycast.data.localData

import com.example.skycast.data.LocalData.ILocalDataSource
import com.example.skycast.data.dataClasses.LocationDataClass
import com.example.skycast.data.dataClasses.NotificationDataClass
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.flow
import java.util.UUID

class LocaldataFake:ILocalDataSource {

    val locations : MutableList<LocationDataClass> =  mutableListOf(
        LocationDataClass(longitude = "12", latitude = "14" , CityName = "arwa",id=21 ),
        LocationDataClass(longitude = "32", latitude = "32" , CityName = "arwa",id=32 ),
        LocationDataClass(longitude = "43", latitude = "43" , CityName = "arwa",id=43 ),
        LocationDataClass(longitude = "54", latitude = "54" , CityName = "arwa",id=54 ),
    )
    override suspend fun getAllFavLocation(): Flow<List<LocationDataClass>> {
    return  flow<List<LocationDataClass>>{ emit(locations) }
    }

    override suspend fun deleteFavLocation(locationDataClass: LocationDataClass): Int {
        TODO("Not yet implemented")
    }


    override suspend fun addFavLocation(locationDataClass: LocationDataClass): Long {
             locations.add(locationDataClass)
        return locationDataClass.id.toLong()
    }

    override suspend fun getAllNotification(): Flow<List<NotificationDataClass>> {
        TODO("Not yet implemented")
    }

    override suspend fun addNotification(notification: NotificationDataClass): Long {
        TODO("Not yet implemented")
    }

    override suspend fun deleteNotification(notificationid: UUID): Int {
        TODO("Not yet implemented")
    }
}