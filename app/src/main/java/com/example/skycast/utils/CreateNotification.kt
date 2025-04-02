package com.example.skycast.utils

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.ContentResolver
import android.content.Context
import android.content.Intent
import android.media.AudioAttributes
import android.net.Uri
import android.os.Build
import androidx.core.app.NotificationCompat
import com.example.skycast.R
import com.example.skycast.MainActivity

private const val CHANNEL_ID = "1"

fun cereateNotification(context: Context): NotificationCompat.Builder {

    // the action that will happend if the user click on the notification
    val intent = Intent(context, MainActivity::class.java)
    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
    val pendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_IMMUTABLE)


    val audioAttributes = AudioAttributes.Builder()
        .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
        .setUsage(AudioAttributes.USAGE_NOTIFICATION)
        .build()
    val notificationSound = Uri.parse("${ContentResolver.SCHEME_ANDROID_RESOURCE}://${context.packageName}/${R.raw.green_woodpecker}")

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        val name = "Time alert"
        val implements = NotificationManager.IMPORTANCE_HIGH
        val channel = NotificationChannel(CHANNEL_ID, name, implements).apply {
            setSound(notificationSound, AudioAttributes.Builder()
                .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                .setUsage(AudioAttributes.USAGE_NOTIFICATION)
                .build()
            )
        }

        val notificationManager =
            context.getSystemService(NotificationManager::class.java) as NotificationManager

        notificationManager.createNotificationChannel(channel)
    }


    val build = NotificationCompat.Builder(context, CHANNEL_ID)
        .setSmallIcon(R.drawable.notification_smal_icon_svgrepo_com)
        .setContentTitle("Sky Cast")
        .setContentText("Hi this is your weather notification")
        .setPriority(NotificationCompat.PRIORITY_HIGH)
        .setContentIntent(pendingIntent)
        .setSound(notificationSound)

    return build
}