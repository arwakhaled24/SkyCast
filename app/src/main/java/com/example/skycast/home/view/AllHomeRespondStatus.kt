package com.example.skycast.home.view

import android.graphics.BlurMaskFilter
import android.graphics.Paint
import android.graphics.RectF
import android.os.Build
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.StartOffset
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
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
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.graphics.BlurEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.skycast.R
import com.example.skycast.data.dataClasses.currentWeather.CurrentWeatherRespond
import com.example.skycast.data.dataClasses.forecastRespond.ForecasteRespond
import com.example.skycast.utils.SharedPrefrances
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale
import kotlin.math.roundToInt

@OptIn(ExperimentalFoundationApi::class)
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun OnHomeSuccess(
    currentWeather: CurrentWeatherRespond,
    forecastRespond: ForecasteRespond,
    isConnected: Boolean
) {
    val context = LocalContext.current
    val unit = getTemperatureSymbol(context)
    println(currentWeather.name)
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
                                text = stringResource(R.string.offline_mode),
                                fontSize = 20.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                            Text(
                                text = stringResource(R.string.limited_functionality_available),
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
                        text = currentWeather.name.ifEmpty { "Your city" },
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
                            text = formatNumbers(
                                getTemperature(
                                    currentWeather.main.temp,
                                    context = context
                                ).toDouble(), context
                            ),
                            fontSize = 64.sp,
                            color = Color.White
                        )
                        if (unit != null) {
                            Text(
                                text = unit,
                                fontSize = 16.sp,
                                color = Color.White,
                                modifier = Modifier.align(Alignment.Bottom)
                            )
                        }
                    }
                    Spacer(Modifier.height(10.dp))
                    Text(
                        text = currentWeather.weather[0].description,
                        fontSize = 20.sp,
                        color = Color.White,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(Modifier.height(10.dp))
                    val isArabic = SharedPrefrances.getInstance(context).getLanguage() == "arabic"
                    val locale = if (isArabic) Locale("ar") else Locale("en")
                    Locale.setDefault(locale)
                    val firstApiFormat =
                        DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss", Locale("en"))
                    val date = LocalDate.parse(forecastRespond.list[0].dtTxt, firstApiFormat)


                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(
                            horizontalArrangement = Arrangement.Center,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(
                                text = formatNumbers(
                                    date.monthValue.toDouble(),
                                    context = context
                                ),
                                fontSize = 16.sp, color = Color.White
                            )
                            Spacer(Modifier.width(5.dp))
                            Text("-", color = Color.White)
                            Text(
                                text = formatNumbers(
                                    date.dayOfMonth.toDouble(),
                                    context = context
                                ),
                                fontSize = 18.sp,
                                color = Color.White
                            )
                            Text("-", color = Color.White)
                            Text(
                                text = formatNumbers(
                                    date.year.toDouble(),
                                    context = context
                                ),
                                fontSize = 16.sp, color = Color.White
                            )
                        }
                    }

                    Spacer(Modifier.height(10.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = stringResource(
                                R.string.h,
                                formatNumbers(
                                    currentWeather.main.tempMax.toInt().toDouble(),
                                    context = context
                                )
                            ),
                            fontSize = 16.sp,
                            color = Color.White
                        )
                        Spacer(Modifier.width(5.dp))
                        Text(
                            text = stringResource(
                                R.string.l,
                                formatNumbers(
                                    currentWeather.main.tempMin.toInt().toDouble(),
                                    context = context
                                )
                            ),
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
                val todayForecast = toDaysForecast(forecastRespond.list)
                val todayHourlyForecast = interpolateHourlyForecast(todayForecast)
                items(24) { index ->
                    val item = todayHourlyForecast[index]
                    val time = getTime(currentWeather.dt).toInt() + index
                    HourlyForecastItem(
                        item,
                        time = time
                    )
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
                        WeatherSubCard(
                            stringResource(R.string.humidity),
                            "${
                                formatNumbers(
                                    currentWeather.main.humidity.toDouble(),
                                    context = context
                                )
                            }%"
                        )
                        WeatherSubCard(
                            stringResource(R.string.clouds),
                            "${
                                formatNumbers(
                                    currentWeather.clouds.all.toDouble(),
                                    context = context
                                )
                            }%"
                        )
                        WeatherSubCard(
                            stringResource(R.string.sun_rise),
                            getTimeWithM(currentWeather.sys.sunrise)
                        )
                    }
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center
                    ) {
                        WeatherSubCard(
                            stringResource(R.string.wind), getWIndSpeed(
                                currentWeather,
                                context = context
                            )
                        )
                        WeatherSubCard(
                            stringResource(R.string.pressure),
                            stringResource(
                                R.string.hpa, formatNumbers(
                                    (currentWeather.main.pressure).toDouble(),
                                    context = context
                                )
                            )

                        )
                        WeatherSubCard(
                            stringResource(R.string.sun_set),
                            getTimeWithM(currentWeather.sys.sunset)
                        )
                    }
                }
            }
        }

        item {
            Text(
                text = stringResource(R.string._5_day_forecast),
                fontSize = 20.sp,
                color = Color.White
            )
        }
        item { Spacer(modifier = Modifier.height(8.dp)) }
        items(forecastRespond.list.size) { index ->
            ForecastItem(forecastRespond.list[index])
        }
        item { Spacer(modifier = Modifier.height(80.dp)) }
    }

}


@Composable
fun OnLoading() {
    var text = "L o a d i n g"
    Column(
        Modifier.fillMaxSize(),
        Arrangement.Center,
        Alignment.CenterHorizontally

    ) {
        val blurList = text.mapIndexed { index, char ->
            if (char == ' ') {
                remember { mutableStateOf(0f) }
            } else {
                val infiniteTransition =
                    rememberInfiniteTransition(label = "infinite transition $index")
                infiniteTransition.animateFloat(
                    initialValue = 10f,
                    targetValue = 1f,
                    animationSpec = infiniteRepeatable(
                        animation = tween(
                            durationMillis = 500,
                            easing = LinearEasing,

                            ),
                        repeatMode = RepeatMode.Reverse,
                        initialStartOffset = StartOffset(
                            offsetMillis = 1000 / text.length * index
                        )
                    ),
                    label = "label",
                )
            }
        }

        Row(
            modifier = Modifier.fillMaxSize(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center,

        ) {
            text.forEachIndexed { index, char ->

                Text(
                    text = char.toString(),
                    color = Color.White,
                    fontSize = 16.sp,
                    modifier = Modifier
                        .graphicsLayer {
                            if (char != ' ') {
                                val blueAmount = blurList[index].value
                                renderEffect = BlurEffect(
                                    radiusX = blueAmount,
                                    radiusY = blueAmount,
                                )
                            }
                        }
                        .then(
                            if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.S) {
                                Modifier.fullContentBlure(
                                    { blurList[index].value.roundToInt() }
                                )
                            } else {
                                Modifier
                            }
                        )
                )
            }

        }

    }
}

private fun Modifier.fullContentBlure(
    blureRadius: () -> Int,
    color: Color = Color.Black
): Modifier {
    return drawWithCache {
        val radius = blureRadius()
        val nativePaint: Paint = Paint().apply {
            isAntiAlias = true
            this.color = color.toArgb()
            if (radius > 0) {
                BlurMaskFilter(
                    radius.toFloat(),
                    BlurMaskFilter.Blur.NORMAL
                )
            }
        }
        onDrawWithContent {
            drawContent()

            drawIntoCanvas { canvas ->
                canvas.save()
                val rect = RectF(0f, 0f, size.width.toFloat(), size.height.toFloat())
                canvas.nativeCanvas.drawRect(rect, nativePaint)
                canvas.restore()

            }
        }

    }

}

@Composable
fun OnError(e: Throwable) {
    val context = LocalContext.current
    Toast.makeText(context, e.message, Toast.LENGTH_SHORT).show()
}


