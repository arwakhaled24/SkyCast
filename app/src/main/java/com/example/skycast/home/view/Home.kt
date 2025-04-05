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
import com.example.skycast.data.RespondStatus
import com.example.skycast.home.viewModel.HomeViewModel
import com.example.skycast.home.viewModel.WeatherViewModel
import com.example.skycast.utils.NetworkConnectivityObserver
import com.example.skycast.utils.SharedPreferences
import java.text.NumberFormat
import java.util.Locale


@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun Home(
    lat: String,
    long: String,
    viewModel: WeatherViewModel,
    homeViewModel: HomeViewModel
) {

    Log.i("TAG", "Home long: $long")
    Log.i("TAG", "Home lat: $lat")

    viewModel.getCurrentWeather(lat = lat, lon = long,)
    viewModel.getForecast(lat = lat, lon = long,)
    val context = LocalContext.current
    val connectivityObserver = remember { NetworkConnectivityObserver(context) }
    val isConnected by connectivityObserver.networkStatus.collectAsState(initial = false)
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
            OnHomeSuccess(currentWeather.result, forecastRespond.result, isConnected,homeViewModel)
    }
}





