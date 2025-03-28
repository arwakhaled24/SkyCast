package com.example.skycast.home.view

import android.os.Build
import android.widget.Toast
import androidx.annotation.RequiresApi
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.skycast.data.RespondStatus
import com.example.skycast.data.dataClasses.currentWeather.CurrentWeatherRespond
import com.example.skycast.data.dataClasses.forecastRespond.ForecasteRespond
import com.example.skycast.home.viewModel.WeatherViewModel
import com.example.skycast.utils.NetworkConnectivityObserver
import kotlinx.coroutines.delay
import java.time.LocalDate
import java.time.format.DateTimeFormatter


@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun Home(
    viewModel:WeatherViewModel,
    lat :String,
    long: String,

) {
    var context = LocalContext.current
   var connectivityObserver= NetworkConnectivityObserver(context)
    var isConnected= connectivityObserver.observe().collectAsState(initial = true)
    viewModel.getCurrentWeather(lat = lat, lon = long)
    viewModel.getForecast(lat = lat, lon = long)
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
            OnHomeSuccess(currentWeather.result, forecastRespond.result,isConnected.value )
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun OnHomeSuccess(currenntWeather: CurrentWeatherRespond, forecasteRespond: ForecasteRespond,isConnected: Boolean) {
    val c = "°C"
    val k = "°K"
    val f = "°F"
    var unit = c
    val currentTime = remember { mutableStateOf(getCurrentTime().toString()) }
    val context = LocalContext.current

    LazyColumn(
        modifier = Modifier
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        item { Spacer(modifier = Modifier.height(25.dp)) }

        item {
            if (!isConnected) {
                Text(
                    text = "You Are In Offline Mode",
                    fontSize = 32.sp,
                    color = Color.Black
                )
            }
        }

        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                shape = RoundedCornerShape(16.dp),
                elevation = CardDefaults.cardElevation(8.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFF1E1E1E))
            ) {
                Text(
                    text = currenntWeather.name,
                    fontSize = 24.sp,
                    color = Color.White
                )


                Spacer(Modifier.height(10.dp))


                Row {
                    Text(
                        text = currenntWeather.main.temp.toInt().toString(),
                        fontSize = 64.sp,
                        color = Color.White
                    )
                    Text(text = unit, fontSize = 16.sp, color = Color.White)
                }


                Spacer(Modifier.height(10.dp))


                Text(
                    text = currenntWeather.weather[0].description,
                    fontSize = 20.sp,
                    color = Color.White
                )
                Spacer(Modifier.height(10.dp))
                val firstApiFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
                val date = LocalDate.parse(forecasteRespond.list[0].dtTxt, firstApiFormat)
                Row {
                    Text(text = date.month.name, fontSize = 16.sp, color = Color.White)
                    Spacer(Modifier.width(5.dp))
                    Text(text = date.year.toString(), fontSize = 16.sp, color = Color.White)
                }
                Text(text = date.dayOfWeek.name, fontSize = 18.sp, color = Color.White)
                Text(text = currentTime.value, fontSize = 18.sp, color = Color.White)
                Spacer(Modifier.height(10.dp))


                Row {
                    Text(
                        text = "H:${currenntWeather.main.tempMax.toInt()}°",
                        fontSize = 16.sp,
                        color = Color.White
                    )
                    Spacer(Modifier.width(5.dp))
                    Text(
                        text = "L:${currenntWeather.main.tempMin.toInt()}°",
                        fontSize = 16.sp,
                        color = Color.White
                    )
                }
            }
        }

        item { Spacer(modifier = Modifier.height(32.dp)) }

        item {
            LazyRow(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceAround
            ) {
                val todayForecast = toDaysForecast(forecasteRespond.list)
                val todayHourlyForecast = interpolateHourlyForecast(todayForecast)
                items(24) { index ->
                    val item = todayHourlyForecast[index]
                    HourlyForecastItem(item, getTime(item.dt).toInt() + index)
                }
            }
        }

        item { Spacer(modifier = Modifier.height(16.dp)) }

        item {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                contentAlignment = Alignment.TopCenter
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center
                    ) {
                        WeatherSubCard("💦 Humidity", "${currenntWeather.main.humidity}%")
                        WeatherSubCard("☁\uFE0F Clouds ", "${currenntWeather.clouds.all}%")
                        WeatherSubCard("🌅 Sun Rise", getTimeWithM(currenntWeather.sys.sunrise))
                    }
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center
                    ) {

                        WeatherSubCard("🌬️ Wind", "${currenntWeather.wind.speed} m/s")
                        WeatherSubCard("🌤️ Pressure", "${currenntWeather.main.pressure} hPa")
                        WeatherSubCard("🌇️ Sun Set", getTimeWithM(currenntWeather.sys.sunset))
                    }
                }
            }
        }

        item {
            Text(text = "5-DAY FORECAST", fontSize = 20.sp, color = Color.White)
        }

        item { Spacer(modifier = Modifier.height(8.dp)) }

        items(forecasteRespond.list.size) { index ->
            val listIndex = (index * 8)
            ForecastItem(forecasteRespond.list[index])
        }
    }
    LaunchedEffect(Unit) {
        while (true) {
            currentTime.value = getCurrentTime()
            delay(60000)
        }
    }
}

@Composable
fun OnLoading() {
    Column(
        Modifier.fillMaxSize(),
        Arrangement.Center,
        Alignment.CenterHorizontally

    ) { CircularProgressIndicator() }
}

@Composable
fun OnError(e: Throwable) {
    val context = LocalContext.current
    Toast.makeText(context, e.message, Toast.LENGTH_SHORT).show()
}





