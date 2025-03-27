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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
import com.example.skycast.data.dataClasses.forecastRespond.WeatherItem
import kotlinx.coroutines.delay
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Date
import java.util.Locale
import kotlin.math.absoluteValue


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

/*fun getTime(forecasteRespond: ForecasteRespond) {
    Timestamp(forecasteRespond.list[0].dtTxt.toLong()).time
}*/

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun OnSuccess(currenntWeather: CurrentWeatherRespond, forecasteRespond: ForecasteRespond) {
    val c = "°C"
    val k = "°K"
    val f = "°F"
    var unit = c
    val currentTime = remember { mutableStateOf(getCurrentTime().toString()) }


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

            Spacer(modifier = Modifier.height(32.dp))
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
            Spacer(modifier = Modifier.height(16.dp))


            Card(
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier
                    .padding(8.dp)
                    .size(80.dp),
                colors = CardDefaults.cardColors(
                    containerColor = Color.White.copy(alpha = 0.2f)
                )
            ) {
                Column {
                    Row {
                        Card() {
                            Column {
                                Text("💦 Humidity")
                                Text(currenntWeather.main.humidity.toString())
                            }
                        }
                        Card() {
                            Column {
                                Text("💦 Humidity")
                                Text(currenntWeather.main.humidity.toString())
                            }
                        }
                    }
                    Row {
                        Card() {
                            Column {
                                Text("💦 Humidity")
                                Text(currenntWeather.main.humidity.toString())
                            }
                        }
                        Card() {
                            Column {
                                Text("💦 Humidity")
                                Text(currenntWeather.main.humidity.toString())
                            }
                        }
                    }
                }

            }


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
fun HourlyForecastItem(wearherItem: WeatherItem, time: Int) {
    Card(
        shape = RoundedCornerShape(12.dp),
        modifier = Modifier
            .padding(8.dp)
            .size(80.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White.copy(alpha = 0.2f)
        )

    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.fillMaxSize(),
        ) {
            val pm = time - 12
            val am = time - 24
            Log.i("TAG", "HourlyForecastItem: ${pm.absoluteValue}")
            Text(
                text = if (time <= 12) "$time AM " else if (time <= 24) "$pm PM" else "$am AM",
                color = Color.White
            )
            Text(text = wearherItem.main.temp.toInt().toString(), color = Color.White)

            Text(text = getIcon(wearherItem.weather[0].icon), fontSize = 24.sp)
        }
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

fun getTime(time: Long): String {
    val sdf = SimpleDateFormat("h", Locale.getDefault())
    return sdf.format(Date(time))
}

/*@Composable
fun dayForecastItem(weatherItem: WeatherItem) {
    Log.i("TAG", "dayForecastItem:${weatherItem.dt} ")
    HourlyForecastItem(weatherItem, getTime(weatherItem.dt))
    HourlyForecastItem(weatherItem, getTime(weatherItem.dt + 3_600_000))
    HourlyForecastItem(weatherItem, getTime(weatherItem.dt + 3_600_000 + 3_600_000))
}*/

@Composable
fun getIcon(iconCode: String): String {
    if (iconCode == "01d") return "☀️"
    else if (iconCode == "01n") return "🌑"
    else if (iconCode == "11d" || iconCode == "11n") return "⛈"
    else if (iconCode == "13d" || iconCode == "13n") return "❄"
    else if (iconCode == "02d") return "🌤️"
    else if (iconCode == "10d") return "🌦️"
    else if (iconCode == "03d" || iconCode == "03n") return "☁️"
    else if (iconCode == "09d" || iconCode == "09n" || iconCode == "10n") return "🌧️"
    else if (iconCode == "04d") return "☁️"
    else if (iconCode == "04n") return "☁︎"
    else if (iconCode == "50d" || iconCode == "50n") return "🌫️"
    else if (iconCode == "02n") return "☁"
    return ""

}


fun toDaysForecast(wearherItem: List<WeatherItem>): List<WeatherItem> {
    val todayForecast = wearherItem.subList(0, 9).toList()
    return todayForecast
}

fun interpolateHourlyForecast(weatherItems: List<WeatherItem>): List<WeatherItem> {
    val interpolatedForecast = mutableListOf<WeatherItem>()

    for (i in 0 until weatherItems.size - 1) {
        val currentItem = weatherItems[i]
        val nextItem = weatherItems[i + 1]

        interpolatedForecast.add(currentItem)


        val tempDiff = (nextItem.main.temp - currentItem.main.temp) / 3
        val pressureDiff = (nextItem.main.pressure - currentItem.main.pressure) / 3
        val humidityDiff = (nextItem.main.humidity - currentItem.main.humidity) / 3


        repeat(2) { j ->
            val interpolatedItem = currentItem.copy(
                dt = currentItem.dt + (j + 1) * 3600,
                dtTxt = "",
                main = currentItem.main.copy(
                    temp = currentItem.main.temp + (j + 1) * tempDiff,
                    pressure = currentItem.main.pressure + (j + 1) * pressureDiff,
                    humidity = currentItem.main.humidity + (j + 1) * humidityDiff.toInt()
                )
            )
            interpolatedForecast.add(interpolatedItem)
        }
    }
    interpolatedForecast.add(weatherItems.last())

    return interpolatedForecast
}