package com.example.skycast.data.LocalData.room

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.skycast.data.dataClasses.LocationDataClass
import com.example.skycast.data.dataClasses.NotificationDataClass
import kotlinx.coroutines.flow.Flow
import java.util.UUID

@Dao
interface Dao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFavLocation(location: LocationDataClass): Long

    @Delete
    suspend fun deleteFavLocation(location: LocationDataClass): Int

    @Query("SELECT * FROM  location ")
    fun getAll(): Flow<List<LocationDataClass>>



    @Query("SELECT * FROM  notification ")
    fun getAllNotification():Flow<List<NotificationDataClass>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addNotification(notification: NotificationDataClass):Long


    @Query("DELETE FROM notification WHERE id = :notificationId")
    suspend fun deleteNotification(notificationId: UUID):Int


}