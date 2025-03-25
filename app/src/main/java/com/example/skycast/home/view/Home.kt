package com.example.skycast.home.view
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp


@Composable
fun Home() {

    Box(modifier = Modifier
        .fillMaxSize()
        .background(Color(0xFF000000))) {

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = " CITY NAME", fontSize = 24.sp, color = Color.White)
            Text(text = "21°C", fontSize = 64.sp, color = Color.White)
            Text(text = "Partly Cloudy", fontSize = 20.sp, color = Color.White)
            Text(text = "H:29° L:15°", fontSize = 16.sp, color = Color.White)
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "Cloudy conditions from 1AM-9AM, with showers expected at 9AM.",
                fontSize = 14.sp,
                color = Color.Gray
            )
            Spacer(modifier = Modifier.height(16.dp))

            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceAround) {
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
