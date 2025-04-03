package com.example.skycast.utils

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.example.skycast.R
import kotlinx.serialization.Serializable
@Serializable
class Constant {

    companion object {
        const val API_KEY: String = "84153d7ef691f4e68d94a9475aa7d827"
        const val BASE_URL: String = "https://api.openweathermap.org/"
        const val WEATHER_ENDPOINT = "data/2.5/weather"
        const val FORECAST_ENDPOINT = "data/2.5/forecast"

        object Parameters {
            const val UNITS = "units"
            const val LONGTUDE = "lon"
            const val LATITUDE = "lat"
            const val LANGUAGE = "lang"
            const val APP_ID = "appid"

            const val TIMESTAMPS_NUM =
                "cnt" //	A number of timestamps, which will be returned in the API response. Learn more
        }

        object MeasurementUnit {
            const val IMPERIAL = "imperial" //For temperature in Fahrenheit use units=imperial
            const val METRIC = "metric" //For temperature in Celsius use units=metric
            const val STANDARD = "standard"// default  Kelvin &&Unit Default: meter/sec
        }

        object Language {
            const val ARABIC = "ar"
            const val ENGLISH = "en"
        }
        class sharedPrefrances(context: Context){
        /*    var TEMPERATURE_UNIT ="Temperature Unit"
            val WIND_SPEED= "Wind Speen"
            val LANGUAGE = "language"
            val LOCATION_SOURCE= "Location Source"
            val METER_Sec="meter/sec"
            val CELSIUS  = "C"
            val KELVIN  ="K"
            val FAHRENHEIT  ="F"
            val ARABIC= "Arabic"
            val ENGLISH= "English"
            val GPS="GPS"
            val MAP= "MAP"*/
        var TEMPERATURE_UNIT =context.getString(R.string.temperature_unit)
            val WIND_SPEED= context.getString(R.string.wind_speed_unit)
            val LANGUAGE = context.getString(R.string.language)
            val LOCATION_SOURCE= context.getString(R.string.edit_location)
            val METER_Sec=context.getString(R.string.m_sec)
            val CELSIUS  = context.getString(R.string.celsius_c)
            val KELVIN  =context.getString(R.string.kelvin_k)
            val FAHRENHEIT  =context.getString(R.string.fahrenheit_f)
            val ARABIC= context.getString(R.string.arabic)
            val ENGLISH= context.getString(R.string.english)
            val GPS= context.getString(R.string.gps)
            val MAP= context.getString(R.string.map)
        }
    }
}