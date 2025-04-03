package com.example.skycast.home.view

import android.os.Build
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.skycast.R
import com.example.skycast.data.RespondStatus
import com.example.skycast.data.dataClasses.Main
import com.example.skycast.data.dataClasses.currentWeather.CurrentWeatherRespond
import com.example.skycast.data.dataClasses.forecastRespond.ForecasteRespond
import com.example.skycast.home.viewModel.WeatherViewModel
import com.example.skycast.utils.Constant
import com.example.skycast.utils.NetworkConnectivityObserver
import com.example.skycast.utils.SharedPrefrances
import kotlinx.coroutines.delay
import java.time.LocalDate
import java.time.format.DateTimeFormatter


@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun Home(
    lat: String,
    long: String,
    viewModel: WeatherViewModel
) {
    viewModel.getCurrentWeather(lat = lat, lon = long)
    viewModel.getForecast(lat = lat, lon = long)
    var context = LocalContext.current
    var connectivityObserver = NetworkConnectivityObserver(context)
    var isConnected = connectivityObserver.observe().collectAsState(initial = true)
    val currentWeather = viewModel.currentWeather.collectAsState().value
    val forecastRespond = viewModel.forecast.collectAsState().value
    val visible = remember { mutableStateOf(false) }
    LaunchedEffect(Unit) { visible.value = true }

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
            OnHomeSuccess(currentWeather.result, forecastRespond.result, isConnected.value)
    }
}


@Composable
fun getWIndSpeed(currenntWeather: CurrentWeatherRespond): String {

    val context = LocalContext.current
    if (SharedPrefrances.getInstance(context)
            .getWindSpeed() != Constant.Companion.sharedPrefrances(context).METER_Sec
    ) {
        return "${"%.2f".format(currenntWeather.wind.speed * 2.23694)} ${stringResource(R.string.mile_hour)}"
    } else {
        return "${currenntWeather.wind.speed} ${stringResource(R.string.m_sec)}"
    }
}
@Composable
fun getTempreture(temp: Double): String {
    val context = LocalContext.current

    if (SharedPrefrances.getInstance(context)
            .getTemperature() == Constant.Companion.sharedPrefrances(context).FAHRENHEIT
    ) {
        return "${((temp * 1.8)+ 32).toInt()}"
    } else if (SharedPrefrances.getInstance(context)
            .getTemperature() == Constant.Companion.sharedPrefrances(context).KELVIN
    ){
        return "${(temp+273.15).toInt()}"
    }
    else{
        return temp.toInt().toString()
    }


}




