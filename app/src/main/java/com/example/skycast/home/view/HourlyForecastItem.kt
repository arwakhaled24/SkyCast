package com.example.skycast.home.view


import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.skycast.R
import com.example.skycast.data.dataClasses.forecastRespond.WeatherItem

@Composable
fun HourlyForecastItem(wearherItem: WeatherItem, time: Int, modifier: Modifier=Modifier) {
  val context= LocalContext.current
    val unit = getTemperatureSymbol(context)
    var timeInt = time
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
                text = if (time <= 12) stringResource(R.string.am, formatNumbers(
                    time.toDouble(),
                    context = context
                ))
                else if (time <= 24) stringResource(
                    R.string.pm, formatNumbers(
                        pm.toDouble(),
                        context = context
                    )
                ) else stringResource(R.string.am, formatNumbers(
                    time.toDouble(),
                    context = context
                )),
                color = Color.White
            )
            Text(text = " ${ formatNumbers(
                getTemperature(
                    wearherItem.main.temp,
                    context = context
                ).toDouble(),
                context = context
            )} $unit", color = Color.White)

            Text(text = getIcon(wearherItem.weather[0].icon), fontSize = 24.sp)
        }
    }
}