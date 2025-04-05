package com.example.skycast.alarm.workManager

import android.annotation.SuppressLint
import android.app.NotificationManager
import android.content.Context
import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.work.CoroutineWorker
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.example.skycast.R
import com.example.skycast.data.LocalData.LocalDataSource
import com.example.skycast.data.LocalData.room.MyDatabase
import com.example.skycast.data.dataClasses.NotificationDataClass
import com.example.skycast.data.dataClasses.currentWeather.CurrentWeatherRespond
import com.example.skycast.data.remoteData.WearherRemoreDataSourse
import com.example.skycast.data.remoteData.retrofit.RetrofitHelper
import com.example.skycast.data.repository.WeatherRepository
import com.example.skycast.utils.cereateNotification
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.runBlocking

class MyWorker(val context: Context, val workerParameters: WorkerParameters) :
    CoroutineWorker(context, workerParameters) {
    val repository by lazy {
        WeatherRepository.getInstance(
            WearherRemoreDataSourse(RetrofitHelper(context)),
            localDataSource = LocalDataSource(MyDatabase.getInstance(context).getDao()),
        )
    }
    private val _currentWeather = MutableStateFlow<CurrentWeatherRespond?>(null)
 
    override suspend fun doWork(): Result {
        val latitude = inputData.getDouble("latitude", 0.0)
        val longitude = inputData.getDouble("longitude", 0.0)

        try {
            getCurrentWeather(
                lat = latitude.toString(),
                lon = longitude.toString(),
            )
            if (_currentWeather?.value?.weather?.get(0)?.description.isNullOrBlank()) {
                showNotification(context.getString(R.string.check_the_weather_now))
            }
            else{
                showNotification(" ${context.getString(R.string.the_current_weather_is)} " +
                        "${ _currentWeather.value?.weather?.get(0)?.description}")
            }

        } catch (e: Exception) {
            showNotification(context.getString(R.string.check_the_weather_now))
        } finally {
            repository.deleteNotification(workerParameters.id)
        }
        return Result.success()
    }

    @SuppressLint("ServiceCast")
    private fun showNotification(status: String) {
        val notificationBuilder = cereateNotification(applicationContext)
        notificationBuilder.setContentText(status)
        val notificationManager =
            applicationContext.getSystemService(Context.NOTIFICATION_SERVICE)
                    as NotificationManager
        val notificationId = 1
        notificationManager.notify(notificationId, notificationBuilder.build())
    }


    fun getCurrentWeather(
        lat: String,
        lon: String,
        language: String = "en",
        unit: String = "metric",
    ) {
        runBlocking(Dispatchers.IO) {
            val result = repository.getCurrentWeather(lat, lon, language, unit)
            result.collect {
                it
                if (it != null) {
                    _currentWeather.value = it
                }
            }
        }
    }

}