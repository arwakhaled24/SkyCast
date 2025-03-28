package com.example.skycast.alarm.service

import android.annotation.SuppressLint
import android.app.IntentService
import android.content.Intent
import android.content.Context
import com.example.skycast.utils.cereateNotification


class MyNotificationService : IntentService("MyNotificationService") {

    @SuppressLint("ForegroundServiceType")
    override fun onHandleIntent(intent: Intent?) {
        startForeground(10,cereateNotification(this).build())
    }


}