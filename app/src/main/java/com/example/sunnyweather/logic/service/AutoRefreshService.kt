package com.example.sunnyweather.logic.service

import android.app.*
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Binder
import android.os.Build
import android.os.IBinder
import android.os.SystemClock
import android.util.Log
import androidx.core.app.NotificationCompat
import com.example.sunnyweather.R
import com.example.sunnyweather.logic.EventMessage
import com.example.sunnyweather.logic.dao.PlaceDao
import com.example.sunnyweather.ui.weather.WeatherActivity
import org.greenrobot.eventbus.EventBus
import kotlin.concurrent.thread

class AutoRefreshService : Service() {
    private lateinit var alarmManager:AlarmManager

    override fun onBind(intent: Intent): IBinder? {
        return null
    }

    override fun onCreate() {
        super.onCreate()
        val manager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                "update_service", "后台自动更新开启",
                NotificationManager.IMPORTANCE_DEFAULT
            )
            manager.createNotificationChannel(channel)
        }

        val notification = NotificationCompat.Builder(this, "update_service")
            .setContentTitle("后台自动更新天气开启中")
            .setContentText("可以进入查看")
            .setSmallIcon(R.drawable.ic_fog)
            .setLargeIcon(BitmapFactory.decodeResource(resources, R.drawable.ic_carwashing))
            .setAutoCancel(true)
            .build()
        startForeground(1, notification)
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        thread {
            Log.d("Time:","task start")
            EventBus.getDefault().post(EventMessage("refresh"))
        }

        alarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val myIntent = Intent(this, AutoUpdateReceiver::class.java)
        val pendingIntent = PendingIntent.getBroadcast(this, 0, myIntent, 0)
        alarmManager.setRepeating(
            AlarmManager.ELAPSED_REALTIME_WAKEUP,
            SystemClock.elapsedRealtime() + 5 * 1000,
            60 * 1000,
            pendingIntent
        )
        return super.onStartCommand(intent, flags, startId)
    }


    override fun onDestroy() {
        val myIntent = Intent(this, AutoUpdateReceiver::class.java)
        val pendingIntent = PendingIntent.getBroadcast(this, 0, myIntent, 0)
        alarmManager.cancel(pendingIntent)
        Log.d("Time:","stop")
        super.onDestroy()
    }

}
