package com.example.skycast.home.viewModel

import android.content.Context
import androidx.lifecycle.ViewModel
import com.example.skycast.R
import com.example.skycast.data.dataClasses.currentWeather.CurrentWeatherRespond
import com.example.skycast.utils.Constant
import com.example.skycast.utils.SharedPreferences
import java.text.NumberFormat
import java.util.Locale

class HomeViewModel :ViewModel() {
    fun getWIndSpeed(currentWeather: CurrentWeatherRespond, context: Context): String {
        return if (SharedPreferences.getInstance(context)
                .getWindSpeed() != Constant.Companion.sharedPrefrances(context).METER_Sec
        ) {
            "${"%.2f".format(currentWeather.wind.speed * 2.23694)} ${context.getString(R.string.mile_hour)}"
        } else {
            "${currentWeather.wind.speed} ${context.getString(R.string.m_sec)}"
        }
    }

    fun getTemperature(temp: Double,context: Context): String {
        return if (SharedPreferences.getInstance(context).getTemperature()
            ==  Constant.Companion.sharedPrefrances(context).FAHRENHEIT) {
            "${((temp * 1.8) + 32).toInt()}"
        } else if (SharedPreferences.getInstance(context).getTemperature()
            == Constant.Companion.sharedPrefrances(context).KELVIN
        ) {
            "${(temp + 273.15).toInt()}"
        } else {
            temp.toInt().toString()
        }
    }


    fun formatNumbers(number: Double,context: Context): String {
        val isArabic = SharedPreferences.getInstance(context).getLanguage() == "arabic"
        val locale = if (isArabic) Locale("ar") else Locale.ENGLISH
        val formatter = NumberFormat.getInstance(locale)
        formatter.isGroupingUsed = false
        return formatter.format(number)
    }





}
