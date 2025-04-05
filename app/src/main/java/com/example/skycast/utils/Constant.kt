package com.example.skycast.utils

import android.content.Context
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

        class sharedPrefrances(context: Context) {
             var TEMPERATURE_UNIT ="Temperature Unit"
             val WIND_SPEED= "Wind Speen"
             val LANGUAGE = "language"
             val LOCATION_SOURCE= "Location Source"
              val METER_Sec="meter/sec"
             val MILE_HOURE="mile/hour"
             val CELSIUS  = "°C"
             val KELVIN  ="°K"
             val FAHRENHEIT  ="°F"
             val ARABIC= "Arabic"
             val ENGLISH= "english"
             val GPS="GPS"
             val MAP= "MAP"
             val SELETED_LAT= "SELECTEDLAT"
             val SELETED_LONG= "SELECTED_LONG"
             val IS_SELECTED_LOCATION="IS_SELECTED_LOCATION"



        }
    }
}