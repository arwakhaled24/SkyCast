package com.example.skycast.data.LocalData

import com.example.skycast.data.LocalData.room.Dao
import com.example.skycast.data.dataClasses.LocationDataClass
import com.example.skycast.data.dataClasses.NotificationDataClass
import kotlinx.coroutines.flow.Flow
import java.util.UUID

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


    override suspend fun getAllNotification(): Flow<List<NotificationDataClass>> {
        return dao.getAllNotification()
    }

   override suspend fun deleteNotification(notificationId: UUID):Int{
        return dao.deleteNotification(notificationId)
    }

    override suspend fun addNotification(notification: NotificationDataClass):Long{
        return dao.addNotification(notification)
    }


}