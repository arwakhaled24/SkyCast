package com.example.skycast.utils

class Constant {
    companion object {
        const val API_KEY: String = "84153d7ef691f4e68d94a9475aa7d827"
        const val BASE_URL: String = "https://api.openweathermap.org/"
        const val WEATHER_ENDPOINT = "data/2.5/weather"
        const val FORECAST_ENDPOINT = "data/2.5/forecast"

        object Parameters {
            const val UNITS = "units"
            const val LANGUAGE = "lang"
            const val LONGTUDE = "lon"
            const val LATITUDE = "lat"
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
    }
}