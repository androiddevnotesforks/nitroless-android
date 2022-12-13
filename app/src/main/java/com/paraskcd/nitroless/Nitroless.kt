package com.paraskcd.nitroless

import android.app.Application
import android.content.SharedPreferences
import android.os.Build
import androidx.annotation.RequiresApi
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class Nitroless: Application() {
    var preferences: SharedPreferences? = null

    @RequiresApi(Build.VERSION_CODES.P)
    override fun onCreate() {
        super.onCreate()
        preferences = getSharedPreferences(getProcessName() + "_preferences", MODE_PRIVATE)
    }
}