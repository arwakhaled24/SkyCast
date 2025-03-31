package com.example.skycast.alarm.workManager

import android.annotation.SuppressLint
import android.app.NotificationManager
import android.content.Context
import android.util.Log
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.example.skycast.utils.cereateNotification

class MyWorker (context: Context, workerParameters: WorkerParameters)
    :Worker(context,workerParameters) {
    override fun doWork(): Result {
        // get the api call
        // show the notification
        // in try catcch
        showNotification()

        return Result.success()
    }
    @SuppressLint("ServiceCast")
    private fun showNotification() {
        val notificationBuilder = cereateNotification(applicationContext)
        notificationBuilder.setContentText("Weather updated successfully!")
        val notificationManager =
            applicationContext.getSystemService(Context.NOTIFICATION_SERVICE)
            as NotificationManager
        val notificationId = 1
        notificationManager.notify(notificationId, notificationBuilder.build())
    }

}