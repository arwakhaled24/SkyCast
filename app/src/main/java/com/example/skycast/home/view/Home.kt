package com.example.skycast.home.view

import android.icu.text.SimpleDateFormat
import android.os.Build
import android.util.Log
import android.widget.Toast
import androidx.annotation.RequiresApi
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
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
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
import kotlinx.coroutines.delay
import java.sql.Timestamp
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Date
import java.util.Locale


@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun Home(
    currenntWeather: RespondStatus<CurrentWeatherRespond>,
    forecasteRespond: RespondStatus<ForecasteRespond>
) {

    when {
        currenntWeather is RespondStatus.Error || forecasteRespond is RespondStatus.Error -> {
            when {
                currenntWeather is RespondStatus.Error -> OnError(currenntWeather.error)
            }
            when {
                forecasteRespond is RespondStatus.Error -> OnError(forecasteRespond.error)
            }
        }

        currenntWeather is RespondStatus.Loading || forecasteRespond is RespondStatus.Loading ->
            OnLoading()

        currenntWeather is RespondStatus.Success && forecasteRespond is RespondStatus.Success ->
            OnSuccess(currenntWeather.result, forecasteRespond.result)
    }
}

fun getTime(forecasteRespond: ForecasteRespond) {
    Timestamp(forecasteRespond.list[0].dtTxt.toLong()).time
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun OnSuccess(currenntWeather: CurrentWeatherRespond, forecasteRespond: ForecasteRespond) {
    val c = "°C"
    val k = "°K"
    val f = "°F"
    var unit = c
   val currentTime  = remember { mutableStateOf(getCurrentTime().toString()) }


    Log.i("TAG", "OnSuccess: $currentTime")
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF000000))
    ) {

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(25.dp))
            Text(
                text = currenntWeather.name,
                fontSize = 24.sp, color = Color.White
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
                Text(
                    text = date.month.name,
                    fontSize = 16.sp,
                    color = Color.White
                )
                Spacer(Modifier.width(5.dp))
                Text(
                    text = date.year.toString(),
                    fontSize = 16.sp,
                    color = Color.White
                )

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

            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "Cloudy conditions from 1AM-9AM," +
                        " with showers expected at 9AM.",
                fontSize = 14.sp,
                color = Color.Gray
            )
            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceAround
            ) {
                HourlyForecastItem("Now", "21°", "☁️")
                HourlyForecastItem("10PM", "21°", "🌧️")
                HourlyForecastItem("11PM", "19°", "🌧️")
                HourlyForecastItem("12AM", "19°", "🌤️")
                HourlyForecastItem("1AM", "19°", "☀️")
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(text = "10-DAY FORECAST", fontSize = 20.sp, color = Color.White)
            Spacer(modifier = Modifier.height(8.dp))

            ForecastItem("Today", "15°", "29°", "☀️", "0%")
            ForecastItem("Mon", "18°", "27°", "🌧️", "60%")
            ForecastItem("Tue", "20°", "25°", "🌧️", "50%")
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

@Composable
fun HourlyForecastItem(time: String, temperature: String, icon: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(text = time, color = Color.White)
        Text(text = temperature, color = Color.White)
        Text(text = icon, fontSize = 24.sp)
    }
}

@Composable
fun ForecastItem(day: String, low: String, high: String, icon: String, chanceOfPrecip: String) {
    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
        Text(text = day, color = Color.White)
        Text(text = low, color = Color.White)
        Text(text = high, color = Color.White)
        Text(text = icon, fontSize = 24.sp)
        Text(text = chanceOfPrecip, color = Color.White)
    }
}

fun getCurrentTime(): String {
    val sdf = SimpleDateFormat("h:mm a", Locale.getDefault())
    return sdf.format(Date(System.currentTimeMillis()))
}