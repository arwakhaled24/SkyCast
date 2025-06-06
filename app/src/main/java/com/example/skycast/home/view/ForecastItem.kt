package com.example.skycast.home.view

import android.annotation.SuppressLint
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.LinearGradient
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.skycast.R
import com.example.skycast.data.dataClasses.forecastRespond.WeatherItem
import com.example.skycast.home.viewModel.HomeViewModel
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.format.TextStyle
import java.util.Locale
@SuppressLint("SuspiciousIndentation")
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun ForecastItem(weatherItem: WeatherItem,homeViewModel: HomeViewModel) {
    val context = LocalContext.current
    val dayTranslations = mapOf(
        Locale("en") to mapOf(
            "MONDAY" to "Monday",
            "TUESDAY" to "Tuesday",
            "WEDNESDAY" to "Wednesday",
            "THURSDAY" to "Thursday",
            "FRIDAY" to "Friday",
            "SATURDAY" to "Saturday",
            "SUNDAY" to "Sunday"
        ),
        Locale("ar") to mapOf(
            "MONDAY" to "الاثنين",
            "TUESDAY" to "الثلاثاء",
            "WEDNESDAY" to "الأربعاء",
            "THURSDAY" to "الخميس",
            "FRIDAY" to "الجمعة",
            "SATURDAY" to "السبت",
            "SUNDAY" to "الأحد"
        )
    )
    val unit =getTemperatureSymbol(context)
    val firstApiFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
    val date: LocalDateTime = LocalDateTime.parse(weatherItem.dtTxt, firstApiFormat)
    val locale = context.resources.configuration.locales[0]
    val dayNameInEnglish = date.dayOfWeek.getDisplayName(TextStyle.FULL, Locale.ENGLISH).toUpperCase(Locale.ENGLISH)
    val translatedDayName = dayTranslations[locale]?.get(dayNameInEnglish) ?: dayNameInEnglish

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp, horizontal = 16.dp)
                .background(Color.Gray.copy(alpha = 0.4f), shape = RoundedCornerShape(12.dp))
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,

            ) {
            Text(
                text =translatedDayName.take(6),
                color = Color.White,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )

            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = "${homeViewModel.formatNumbers(
                        homeViewModel.getTemperature(
                            weatherItem.main.temp,
                            context = context
                        ).toDouble(),
                        context = context,
                    )} $unit",
                    color = Color.White,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Medium
                )
                Text(
                    text = stringResource(
                        R.string.h_l,
                        homeViewModel.formatNumbers(
                            homeViewModel.getTemperature(
                                weatherItem.main.tempMax,
                                context = context
                            ).toDouble(),
                            context = context
                        ),
                        homeViewModel.formatNumbers(
                            homeViewModel.getTemperature(
                                weatherItem.main.tempMin,
                                context = context
                            ).toDouble(),
                            context = context
                        )
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

