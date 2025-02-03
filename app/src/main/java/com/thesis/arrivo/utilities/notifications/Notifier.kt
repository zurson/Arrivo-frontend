package com.thesis.arrivo.utilities.notifications

import android.Manifest
import android.annotation.SuppressLint
import android.app.AlarmManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import java.time.LocalDateTime
import java.time.ZoneId

object Notifier {
    private const val CHANNEL_ID = "notify_channel"
    private lateinit var appContext: Context


    fun init(context: Context) {
        appContext = context.applicationContext
        createNotificationChannel()
    }


    private fun createNotificationChannel() {
        val name = "Notifications"
        val descriptionText = "Channel for app notifications"
        val importance = NotificationManager.IMPORTANCE_DEFAULT
        val channel = NotificationChannel(CHANNEL_ID, name, importance).apply {
            description = descriptionText
        }
        val notificationManager: NotificationManager =
            appContext.getSystemService(NotificationManager::class.java)
        notificationManager.createNotificationChannel(channel)
    }


    fun sendNotification(title: String, message: String, notificationId: Int) {
        val builder = NotificationCompat.Builder(appContext, CHANNEL_ID)
            .setSmallIcon(android.R.drawable.ic_dialog_info)
            .setContentTitle(title)
            .setContentText(message)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)

        notifyUser(notificationId, builder)
    }


    private fun notifyUser(id: Int, builder: NotificationCompat.Builder) {
        with(NotificationManagerCompat.from(appContext)) {
            if (ActivityCompat.checkSelfPermission(
                    appContext,
                    Manifest.permission.POST_NOTIFICATIONS
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                return
            }

            notify(id, builder.build())
        }
    }


    fun fastNotification(title: String, message: String) {
        val notificationId = NotificationIdManager.getNextNotificationId(context = appContext)
        sendNotification(title, message, notificationId)
    }


    @SuppressLint("ScheduleExactAlarm")
    private fun scheduleNotification(title: String, message: String, timeInMillis: Long): Int {
        val notificationId = NotificationIdManager.getNextNotificationId(context = appContext)

        val intent = Intent(appContext, NotificationReceiver::class.java).apply {
            putExtra("title", title)
            putExtra("message", message)
            putExtra("notification_id", notificationId)
        }

        val pendingIntent = PendingIntent.getBroadcast(
            appContext,
            notificationId,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val alarmManager = appContext.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        alarmManager.setExact(AlarmManager.RTC_WAKEUP, timeInMillis, pendingIntent)
        return notificationId
    }


    fun scheduleNotificationIn(
        title: String,
        message: String,
        seconds: Int,
        minutes: Int,
        hours: Int
    ): Int {
        val timeInMillis =
            System.currentTimeMillis() + (seconds * 1000L) + (minutes * 60 * 1000L) + (hours * 60 * 60 * 1000L)
        return scheduleNotification(title, message, timeInMillis)
    }


    fun scheduleNotificationAt(
        title: String,
        message: String,
        dateTime: LocalDateTime
    ): Int {
        val timeInMillis = dateTime.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli()
        return scheduleNotification(title, message, timeInMillis)
    }


    fun cancelScheduledNotification(notificationId: Int) {
        val intent = Intent(appContext, NotificationReceiver::class.java)
        val pendingIntent = PendingIntent.getBroadcast(
            appContext,
            notificationId,
            intent,
            PendingIntent.FLAG_NO_CREATE or PendingIntent.FLAG_IMMUTABLE
        )
        if (pendingIntent != null) {
            val alarmManager = appContext.getSystemService(Context.ALARM_SERVICE) as AlarmManager
            alarmManager.cancel(pendingIntent)
            pendingIntent.cancel()
        }
    }
}


class NotificationReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        val title = intent.getStringExtra("title") ?: "Notification"
        val message = intent.getStringExtra("message") ?: "You have a notification"
        val notificationId = intent.getIntExtra("notification_id", 1001)
        Notifier.sendNotification(title, message, notificationId)
    }
}
