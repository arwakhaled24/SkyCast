package com.example.skycast.utils

import android.content.Context
import android.content.SharedPreferences
import com.example.skycast.R

class SharedPrefrances private constructor( val context: Context){

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

     fun setTemperature(temperature: String) {
        editor!!.putString(sharedPreferencesConstants.TEMPERATURE_UNIT, temperature)
        editor!!.commit()
    }
     fun getTemperature() = sharedPreferences!!.getString(sharedPreferencesConstants.TEMPERATURE_UNIT
         ,sharedPreferencesConstants.CELSIUS)!!




     fun setWindSpeed(windSpeed: String){
        editor!!.putString(sharedPreferencesConstants.WIND_SPEED, windSpeed)
        editor!!.commit()
    }
     fun getWindSpeed() = sharedPreferences!!.getString(sharedPreferencesConstants.WIND_SPEED,
         sharedPreferencesConstants.METER_Sec)!!



    
     fun setLanguage(language: String){
        editor!!.putString(sharedPreferencesConstants.LANGUAGE, language)
        editor!!.commit()
    }
     fun getLanguage() = sharedPreferences!!.getString(sharedPreferencesConstants.LANGUAGE, sharedPreferencesConstants.ENGLISH)!!




    fun setLocationSource(language: String){
        editor!!.putString(sharedPreferencesConstants.LOCATION_SOURCE, language)
        editor!!.commit()
    }
    fun getLocationSource() = sharedPreferences!!.getString(sharedPreferencesConstants.LOCATION_SOURCE,
        sharedPreferencesConstants.GPS)!!

}