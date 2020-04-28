package com.example.sunnyweather.logic.service

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import com.example.sunnyweather.logic.EventMessage
import org.greenrobot.eventbus.EventBus

class AutoUpdateReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        val myIntent = Intent(context, AutoRefreshService::class.java)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            context.startForegroundService(myIntent)
        } else {
            context.startService(
                myIntent
            )
        }
    }
}
