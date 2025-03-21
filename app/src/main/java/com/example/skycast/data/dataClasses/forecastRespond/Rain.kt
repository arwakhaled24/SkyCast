package com.example.skycast.data.dataClasses.forecastRespond

import com.google.gson.annotations.SerializedName

data class Rain(
    @SerializedName("3h")
    val n3h: Double,
)