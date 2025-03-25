package com.example.skycast.data.LocalData.room

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.skycast.data.dataClasses.LocationDataClass
import kotlinx.coroutines.flow.Flow
@Dao
interface Dao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFavLocation(location: LocationDataClass): Long

    @Delete
    suspend fun deleteFavLocation(location: LocationDataClass): Int

    @Query("SELECT * FROM  location ")
    fun getAll(): Flow<List<LocationDataClass>>


}