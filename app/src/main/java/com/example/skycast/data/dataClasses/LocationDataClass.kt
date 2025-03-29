package com.example.skycast.data.dataClasses

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity("location")
data class LocationDataClass(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val longitude: String,
    val latitude: String,
    val CityName: String,
)
