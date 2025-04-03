package com.example.skycast.home.view

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.collection.longListOf
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
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


@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun Home(
    lat: String,
    long: String,
    viewModel: WeatherViewModel
) {
    val context = LocalContext.current

    Log.i("TAG", "Home: $long")
    Log.i("TAG", "Home: $lat")

    viewModel.getCurrentWeather(lat = lat, lon = long,)
    viewModel.getForecast(lat = lat, lon = long,)
    val connectivityObserver = remember { NetworkConnectivityObserver(context) }
    val isConnected by connectivityObserver.networkStatus.collectAsState(initial = true)
    val currentWeather = viewModel.currentWeather.collectAsState().value
    val forecastRespond = viewModel.forecast.collectAsState().value


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


@Composable
fun getWIndSpeed(currenntWeather: CurrentWeatherRespond): String {

    val context = LocalContext.current
   return if (SharedPrefrances.getInstance(context)
            .getWindSpeed() != Constant.Companion.sharedPrefrances(context).METER_Sec
    ) {
         "${"%.2f".format(currenntWeather.wind.speed * 2.23694)} ${stringResource(R.string.mile_hour)}"
    } else {
         "${currenntWeather.wind.speed} ${stringResource(R.string.m_sec)}"
    }
}

@Composable
fun getTempreture(temp: Double): String {
    val context = LocalContext.current


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




