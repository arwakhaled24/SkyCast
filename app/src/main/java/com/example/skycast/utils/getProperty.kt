package com.example.skycast.utils

import android.content.Context
import android.content.pm.PackageManager

fun getMetaDataValue(context: Context, key: String="com.google.android.geo.API_KEY"): String? {
    return try {
        val appInfo = context.packageManager.getApplicationInfo(context.packageName, PackageManager.GET_META_DATA)
        appInfo.metaData?.getString(key)
    } catch (e: PackageManager.NameNotFoundException) {
        null
    }
}
