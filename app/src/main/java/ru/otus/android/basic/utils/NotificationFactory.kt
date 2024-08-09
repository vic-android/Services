package ru.otus.android.basic.utils

import android.app.Notification
import android.app.Notification.Builder
import android.app.PendingIntent
import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat.FOREGROUND_SERVICE_IMMEDIATE
import ru.otus.android.basic.service.R

class NotificationFactory(
    private val context: Context,
    private val channelId: String = "channelId",
    private val pendingIntent: PendingIntent? = null,
) {

    private var lastTitle = ""
    private var lastText = ""

    fun create(
        title: String? = null,
        text: String? = null,
    ): Notification {
        context.createNotificationChannel(channelId)
        return context.createNotification(
            title = title?.also { lastTitle = it } ?: lastTitle,
            text = text?.also { lastText = it } ?: lastTitle,
            channelId = channelId,
            pendingIntent = pendingIntent,
        )
    }

    private fun Context.createNotification(
        title: String,
        text: String,
        channelId: String,
        pendingIntent: PendingIntent?,
    ): Notification {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            Builder(this, channelId)
                .setContentTitle(title)
                .setContentText(text)
                .setForegroundServiceBehavior(Notification.FOREGROUND_SERVICE_IMMEDIATE)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentIntent(pendingIntent)
                .build()
        } else {
            Builder(this, channelId)
                .setContentTitle(title)
                .setContentText(text)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentIntent(pendingIntent)
                .build()
        }

    }
}