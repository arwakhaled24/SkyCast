package com.example.skycast.home.view

import android.content.Context
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import com.example.skycast.R
import com.example.skycast.data.RespondStatus
import com.example.skycast.data.dataClasses.currentWeather.CurrentWeatherRespond
import com.example.skycast.home.viewModel.WeatherViewModel
import com.example.skycast.utils.Constant
import com.example.skycast.utils.NetworkConnectivityObserver
import com.example.skycast.utils.SharedPrefrances
import java.text.NumberFormat
import java.util.Locale


@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun Home(
    lat: String,
    long: String,
    viewModel: WeatherViewModel
) {

    Log.i("TAG", "Home long: $long")
    Log.i("TAG", "Home lat: $lat")

    viewModel.getCurrentWeather(lat = lat, lon = long,)
    viewModel.getForecast(lat = lat, lon = long,)
    val context = LocalContext.current
    val connectivityObserver = remember { NetworkConnectivityObserver(context) }
    val isConnected by connectivityObserver.networkStatus.collectAsState(initial = true)
    val currentWeather = viewModel.currentWeather.collectAsState().value
    val forecastRespond = viewModel.forecast.collectAsState().value

/*    OnLoading()*/
    when {
        currentWeather is RespondStatus.Error || forecastRespond is RespondStatus.Error -> {
            when {
                currentWeather is RespondStatus.Error -> OnError(currentWeather.error)
            }
            when {
                forecastRespond is RespondStatus.Error -> OnError(forecastRespond.error)
            }
        }
        currentWeather is RespondStatus.Loading || currentWeather is RespondStatus.Loading ->
            OnLoading()
        currentWeather is RespondStatus.Success && forecastRespond is RespondStatus.Success ->
            OnHomeSuccess(currentWeather.result, forecastRespond.result, isConnected)
    }
}


fun getWIndSpeed(currentWeather: CurrentWeatherRespond,context: Context): String {
   return if (SharedPrefrances.getInstance(context)
            .getWindSpeed() != Constant.Companion.sharedPrefrances(context).METER_Sec
    ) {
         "${"%.2f".format(currentWeather.wind.speed * 2.23694)} ${context.getString(R.string.mile_hour)}"
    } else {
         "${currentWeather.wind.speed} ${context.getString(R.string.m_sec)}"
    }
}

fun getTemperature(temp: Double,context: Context): String {
    return if (SharedPrefrances.getInstance(context).getTemperature()
        ==  Constant.Companion.sharedPrefrances(context).FAHRENHEIT) {
        "${((temp * 1.8) + 32).toInt()}"
    } else if (SharedPrefrances.getInstance(context).getTemperature()
        == Constant.Companion.sharedPrefrances(context).KELVIN
    ) {
        "${(temp + 273.15).toInt()}"
    } else {
        temp.toInt().toString()
    }
}


fun formatNumbers(number: Double,context: Context): String {
    val isArabic = SharedPrefrances.getInstance(context).getLanguage() == "arabic"
    val locale = if (isArabic) Locale("ar") else Locale.ENGLISH
    val formatter = NumberFormat.getInstance(locale)
    formatter.isGroupingUsed = false
    return formatter.format(number)
}


