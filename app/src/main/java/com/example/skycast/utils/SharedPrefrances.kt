package com.example.skycast.utils

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import androidx.annotation.StringRes
import com.example.skycast.data.dataClasses.LocationDataClass

class SharedPrefrances private constructor(val context: Context) {

    private var sharedPreferences: SharedPreferences? = null
    private var editor: SharedPreferences.Editor? = null
    private var sharedPreferencesConstants = Constant.Companion.sharedPrefrances(context)

    init {
        sharedPreferences = context.getSharedPreferences("sharedPreferences", Context.MODE_PRIVATE)
        editor = sharedPreferences?.edit()
    }

    companion object {
        @Volatile
        private var instance : SharedPrefrances? = null

        fun getInstance(context: Context) : SharedPrefrances {
            return instance ?: synchronized(context){
                val instance = SharedPrefrances(context)
                this.instance = instance

                instance
            }

        }
    }


    fun setTemperature( temperature: String) {
        editor!!.putString("tempUnit", temperature)
        editor!!.commit()
    }

    fun getTemperature() = sharedPreferences!!.getString("tempUnit", sharedPreferencesConstants.CELSIUS)!!


    fun setWindSpeed( windSpeed: String) {
        editor!!.putString("windSpeed", windSpeed)
        editor!!.commit()
    }

    fun getWindSpeed() = sharedPreferences!!.getString("windSpeed", sharedPreferencesConstants.METER_Sec)!!


    fun setLanguage( language: String) {
        editor!!.putString("language", language)
        editor!!.commit()
    }

    fun getLanguage() = sharedPreferences!!.getString("language", sharedPreferencesConstants.ENGLISH)!!


    fun setLocationSource( language: String) {
        editor!!.putString("locationRes", language)
        editor!!.commit()
    }

    fun getLocationSource() = sharedPreferences!!.getString("locationRes", sharedPreferencesConstants.GPS)!!



        fun inMap(locationDataClass: LocationDataClass) {
            editor!!.putString("Lat", locationDataClass.latitude)
            editor!!.putString("Long" ,locationDataClass.longitude)
            editor!!.commit()
        }

        fun getLat() = sharedPreferences!!.getString(
            "Lat",
          "0"
        )!!

        fun getLong() = sharedPreferences!!.getString(
            "Long",
            "0"
        )!!

        fun selectedLocation(isSelectedLocation: Boolean) {
            editor!!.putBoolean(sharedPreferencesConstants.IS_SELECTED_LOCATION, isSelectedLocation)
            editor!!.commit()
        }

        fun isSelectedLocation() =
            sharedPreferences!!.getBoolean(sharedPreferencesConstants.IS_SELECTED_LOCATION, false)!!

}

