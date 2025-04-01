package com.example.skycast.data.LocalData.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.skycast.data.dataClasses.LocationDataClass
import com.example.skycast.data.dataClasses.NotificationDataClass

@Database(entities = arrayOf(LocationDataClass::class,NotificationDataClass::class), version = 1)
abstract class MyDatabase : RoomDatabase() {

    abstract fun getDao(): Dao

    companion object {
        @Volatile
        private var INSTANCE: MyDatabase? = null
        fun getInstance(context: Context): MyDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context, MyDatabase::class.java, "myDataBase"
                ).build()
                INSTANCE = instance

                instance
            }
        }
    }



}