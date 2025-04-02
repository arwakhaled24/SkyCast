package com.example.skycast.home.view

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.skycast.data.dataClasses.forecastRespond.WeatherItem
import com.example.skycast.utils.SharedPrefrances
import kotlin.math.absoluteValue

@Composable
fun HourlyForecastItem(wearherItem: WeatherItem, time: Int ) {
  val context= LocalContext.current
    var unit = SharedPrefrances.getInstance(context).getTemperature()
    OutlinedCard (
        shape = RoundedCornerShape(12.dp),
        modifier = Modifier
            .padding(8.dp)
            .size(100.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.Gray.copy(alpha = 0.6f)
        )
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.fillMaxSize(),
        ) {
            val pm = time - 12
            val am = time - 24
            Text(
                text = if (time <= 12) "$time AM " else if (time <= 24) "$pm PM" else "$am AM",
                color = Color.White
            )
            Text(text = " ${getTempreture(wearherItem.main.temp)} $unit", color = Color.White)

            Text(text = getIcon(wearherItem.weather[0].icon), fontSize = 24.sp)
        }
    }
}