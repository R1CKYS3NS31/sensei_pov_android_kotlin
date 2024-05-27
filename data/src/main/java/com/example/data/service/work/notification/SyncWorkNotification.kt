package com.example.data.service.work.notification

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.work.Constraints
import androidx.work.ForegroundInfo
import androidx.work.NetworkType
import com.example.data.R

private const val SYNC_TOPIC = "sync"
private const val SYNC_NOTIFICATION_ID = 0
private const val SYNC_NOTIFICATION_CHANNEL_ID = "sync_notification_channel"

val syncConstraints
    get() = Constraints.Builder()
        .setRequiredNetworkType(NetworkType.CONNECTED).build()

private fun Context.syncWorkNotification(): Notification {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        val channel = NotificationChannel(
            SYNC_NOTIFICATION_CHANNEL_ID, getString(R.string.sync_notification_channel_name),
            NotificationManager.IMPORTANCE_DEFAULT
        ).apply {
            description = getString(R.string.sync_notification_channel_description)
        }
        val notificationManager: NotificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(channel)
    }
    return NotificationCompat.Builder(this, SYNC_NOTIFICATION_CHANNEL_ID)
        .setSmallIcon(R.drawable.ic_launcher_foreground).setContentTitle(
            getString(
                R.string.app_name
            )
        ).setPriority(NotificationCompat.PRIORITY_DEFAULT).build()
}

fun Context.syncForegroundInfo() = ForegroundInfo(SYNC_NOTIFICATION_ID, syncWorkNotification())