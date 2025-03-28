package com.example.skycast.utils

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import com.example.skycast.R
import com.example.skycast.view.MainActivity

private const val CHANNEL_ID = "1"

fun cereateNotification(context: Context): NotificationCompat.Builder {

    // the action that will happend if the user click on the notification
    val intent = Intent(context, MainActivity::class.java)
    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
    val pendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_IMMUTABLE)


    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        val name = "Time alert"
        val implements = NotificationManager.IMPORTANCE_DEFAULT
        val channel = NotificationChannel(CHANNEL_ID, name, implements)

        val notificationManager =
            context.getSystemService(NotificationManager::class.java) as NotificationManager

        notificationManager.createNotificationChannel(channel)
    }
    val build = NotificationCompat.Builder(
        context, CHANNEL_ID
    ).setSmallIcon(R.drawable.notification_smal_icon_svgrepo_com)
        .setContentTitle("Sky Cast")
        .setContentText("Hi this is your weather notification")
        .setContentIntent(pendingIntent)

    return build
}