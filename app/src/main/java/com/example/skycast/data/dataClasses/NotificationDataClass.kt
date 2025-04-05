package com.example.skycast.data.dataClasses

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.UUID


@Entity("notification")
data class NotificationDataClass(@PrimaryKey val id : UUID, val time:String, val date :String)
