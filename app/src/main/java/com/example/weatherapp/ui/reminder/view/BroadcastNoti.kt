package com.example.weatherapp.ui.reminder.view

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Build
import com.example.weatherapp.R


class BroadcastNoti : BroadcastReceiver() {
    companion object {
        const val NOTIFICATION_ID = "notification-id"
        const val NOTIFICATION = "notification"
    }


    private lateinit var notificationManager: NotificationManager
    private lateinit var notificationChannel: NotificationChannel
    private lateinit var builder: Notification.Builder
    private val channelId = "i.apps.notifications"
    private val description = "Test notification"


    override fun onReceive(context: Context, intent: Intent) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationChannel = NotificationChannel(channelId, description, NotificationManager.IMPORTANCE_HIGH)
            notificationChannel.enableLights(true)
            notificationChannel.lightColor = Color.GREEN
            notificationChannel.enableVibration(false)
            notificationManager.createNotificationChannel(notificationChannel)

            builder = Notification.Builder(context, channelId)
                .setContentTitle("Weather app")
                .setSmallIcon(R.drawable.ic_baseline_notifications_off_24)
                .setContentText("weather in this location is good")

        } else {

            builder = Notification.Builder(context)
                .setContentTitle("Weather app")
                .setContentText("weather in this location is good")
                .setSmallIcon(R.drawable.ic_baseline_notifications_off_24)
        }
        notificationManager.notify(1234, builder.build())
    }
}