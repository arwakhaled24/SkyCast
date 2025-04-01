package com.example.skycast.data.LocalData

import com.example.skycast.data.dataClasses.LocationDataClass
import com.example.skycast.data.dataClasses.NotificationDataClass
import kotlinx.coroutines.flow.Flow
import java.util.UUID

interface ILocalDataSource {
    suspend fun getAllFavLocation(): Flow<List<LocationDataClass>>
    suspend fun deleteFavLocation(locationDataClass: LocationDataClass): Int
    suspend fun addFavLocation(locationDataClass: LocationDataClass): Long


    suspend fun getAllNotification(): Flow<List<NotificationDataClass>>
    suspend fun addNotification(notification: NotificationDataClass): Long
    suspend fun deleteNotification(notificationid: UUID): Int

}