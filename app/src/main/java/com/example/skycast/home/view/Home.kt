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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
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
    var context = LocalContext.current
    var connectivityObserver = NetworkConnectivityObserver(context)
    var isConnected = connectivityObserver.observe().collectAsState(initial = true)
    viewModel.getCurrentWeather(lat = lat, lon = long)
    viewModel.getForecast(lat = lat, lon = long)
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

@OptIn(ExperimentalFoundationApi::class)
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun OnHomeSuccess(
    currenntWeather: CurrentWeatherRespond,
    forecasteRespond: ForecasteRespond,
    isConnected: Boolean
) {

    val currentTime = remember { mutableStateOf(getCurrentTime().toString()) }
    val context = LocalContext.current
    var unit = SharedPrefrances.getInstance(context).getTemperature()
    println( getTime(1743573600))
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFA5BFCC)),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        item { Spacer(modifier = Modifier.height(25.dp)) }

        item {
            if (!isConnected) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            color = Color.Red.copy(alpha = 0.5f),
                            shape = RoundedCornerShape(8.dp)
                        )
                        .padding(16.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center,
                        modifier = Modifier.fillMaxWidth()
                    ) {

                        Spacer(modifier = Modifier.width(16.dp))

                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(
                                text = "OFFLINE MODE",
                                fontSize = 20.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                            Text(
                                text = "Limited functionality available",
                                fontSize = 14.sp,
                                color = Color.White.copy(alpha = 0.9f)
                            )
                        }
                    }
                }

            }
        }

        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                shape = RoundedCornerShape(16.dp),
                elevation = CardDefaults.cardElevation(8.dp),
                colors = CardDefaults.cardColors(
                    containerColor = Color.Gray.copy(alpha = 0.3f)
                )
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = currenntWeather.name,
                        fontSize = 24.sp,
                        color = Color.White,
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Center
                    )

                    Spacer(Modifier.height(10.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = getTempreture(currenntWeather.main.temp),
                            fontSize = 64.sp,
                            color = Color.White
                        )
                        Text(
                            text = unit,
                            fontSize = 16.sp,
                            color = Color.White,
                            modifier = Modifier.align(Alignment.Bottom)
                        )
                    }

                    Spacer(Modifier.height(10.dp))

                    Text(
                        text = currenntWeather.weather[0].description,
                        fontSize = 20.sp,
                        color = Color.White,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(Modifier.height(10.dp))

                    val firstApiFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
                    val date = LocalDate.parse(forecasteRespond.list[0].dtTxt, firstApiFormat)

                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(
                            horizontalArrangement = Arrangement.Center,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(text = date.month.name, fontSize = 16.sp, color = Color.White)
                            Spacer(Modifier.width(5.dp))
                            Text(text = date.year.toString(), fontSize = 16.sp, color = Color.White)
                        }

                        Text(
                            text = date.dayOfWeek.name,
                            fontSize = 18.sp,
                            color = Color.White
                        )
                        Text(
                            text = currentTime.value,
                            fontSize = 18.sp,
                            color = Color.White
                        )
                    }

                    Spacer(Modifier.height(10.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center
                    ) {
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
                    val time = getTime(item.dt).toInt() + index
                    HourlyForecastItem(item, time )
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

                        WeatherSubCard("🌬️ Wind", getWIndSpeed(currenntWeather))
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
        item { Spacer(modifier = Modifier.height(80.dp)) }
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

@Composable
fun getWIndSpeed(currenntWeather: CurrentWeatherRespond): String {
    val context = LocalContext.current
    if (SharedPrefrances.getInstance(context)
            .getWindSpeed() != Constant.Companion.sharedPrefrances(context).METER_Sec
    ) {
        return "${"%.2f".format(currenntWeather.wind.speed * 2.23694)} mph"
    } else {
        return "${currenntWeather.wind.speed} m/s"
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




