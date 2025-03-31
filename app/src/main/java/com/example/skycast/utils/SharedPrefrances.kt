package com.example.skycast.utils

import android.content.Context
import android.content.SharedPreferences

class SharedPrefrances private constructor(context: Context){

    private var sharedPreferences: SharedPreferences? = null
    private var editor: SharedPreferences.Editor? = null

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
        editor!!.putString(Constant.Companion.Parameters.TEMPERATURE, temperature)
        editor!!.commit()
    }
     fun getTemperature() = sharedPreferences!!.getString(Constant.Companion.Parameters.TEMPERATURE, "metric")!!




     fun setWindSpeed(windSpeed: String){
        editor!!.putString(Constant.Companion.Parameters.windSpeed, windSpeed)
        editor!!.commit()
    }
     fun getWindSpeed() = sharedPreferences!!.getString(Constant.Companion.Parameters.windSpeed, "metric")!!



    
     fun setLanguage(language: String){
        editor!!.putString(Constant.Companion.Parameters.LANGUAGE, language)
        editor!!.commit()
    }
     fun getLanguage() = sharedPreferences!!.getString(Constant.Companion.Parameters.LANGUAGE, "en")!!



     fun setUnit(unit:String){
        editor!!.putString(Constant.Companion.Parameters.UNITS, unit)
        editor!!.commit()
    }
     fun getUnit() = sharedPreferences?.getString(Constant.Companion.Parameters.UNITS,"metric").toString()









}