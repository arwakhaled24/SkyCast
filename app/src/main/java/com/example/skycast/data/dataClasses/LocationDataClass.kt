package com.example.skycast.data.dataClasses

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable

@Serializable
@Entity("location")
data class LocationDataClass(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val longitude: String,
    val latitude: String,
    val CityName: String,
)
