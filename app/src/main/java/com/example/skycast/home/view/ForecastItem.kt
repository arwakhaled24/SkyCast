package com.example.skycast.home.view

import android.annotation.SuppressLint
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.skycast.R
import com.example.skycast.data.dataClasses.forecastRespond.WeatherItem
import com.example.skycast.utils.SharedPrefrances
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@SuppressLint("SuspiciousIndentation")
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun ForecastItem(weatherItem: WeatherItem) {
    val context= LocalContext.current
    var unit = SharedPrefrances.getInstance(context).getTemperature()
    val firstApiFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
    val date = LocalDate.parse(weatherItem.dtTxt, firstApiFormat)
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp, horizontal = 16.dp)
                .background(Color.Gray.copy(alpha = 0.6f), shape = RoundedCornerShape(12.dp))
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,

            ) {
            Text(
                text = date.dayOfWeek.name.take(3),
                color = Color.White,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )

            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = "${getTempreture(weatherItem.main.temp)} $unit",
                    color = Color.White,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Medium
                )
                Text(
                    text = stringResource(
                        R.string.h_l,
                        getTempreture(weatherItem.main.tempMax),
                        getTempreture(weatherItem.main.tempMin)
                    ),
                    color = Color.White,
                    fontSize = 14.sp
                )
            }

            Text(
                text = getIcon(weatherItem.weather[0].icon),
                fontSize = 28.sp,
                modifier = Modifier.padding(end = 4.dp)
            )
        }


}
