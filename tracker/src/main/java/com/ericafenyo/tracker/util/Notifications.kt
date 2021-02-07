package com.ericafenyo.tracker.util

import android.annotation.TargetApi
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.annotation.DrawableRes
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.ericafenyo.tracker.logger.Logger

object Notifications {
  private const val TAG = "Notification"
  private const val DEFAULT_CHANNEL_ID = "TrackerNotificationChannel"
  private const val DEFAULT_CHANNEL_DESCRIPTION = "Notification channel used the tracker"

  fun create(context: Context, config: Config) {
    val (notificationId, title, message, icon) = config

    val notificationStyle = NotificationCompat.BigTextStyle()
      .bigText(message)
      .setBigContentTitle(title)

    val activityIntent = getLauncherIntent(context)
    val activityPendingIntent = PendingIntent.getActivity(
      context, 0, activityIntent, PendingIntent.FLAG_UPDATE_CURRENT
    )

    createNotificationChannel(context)

    NotificationCompat.Builder(context, DEFAULT_CHANNEL_ID)
      .setSmallIcon(icon)
      .setStyle(notificationStyle)
      .setContentIntent(activityPendingIntent)
      .setContentTitle(title)
      .setPriority(NotificationCompat.PRIORITY_DEFAULT)
      .setContentText(message)
      .setOngoing(true)
      .build()
      .also {
        NotificationManagerCompat.from(context).notify(notificationId, it)
      }
    Logger.debug(
      context, TAG, "Generating notify with id $notificationId and message $message"
    )
  }

  fun cancel(context: Context, notificationId: Int) {
    val manager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
    Logger.debug(context, TAG, "Cancelling notify with id $notificationId")
    manager.cancel(notificationId)
  }

  @TargetApi(26)
  private fun createNotificationChannel(context: Context) {
    // Only call on Android O and above
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
      val manager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
      for (chanel in manager.notificationChannels) {
        Logger.debug(context, TAG, "Checking channel with id = ${chanel.id}")
        if (DEFAULT_CHANNEL_ID == chanel.id) {
          Logger.debug(context, TAG, "Default channel found, returning")
          return
        }
      }
      Logger.debug(context, TAG, "Default channel not found, creating a new one")
      val channel = NotificationChannel(
        DEFAULT_CHANNEL_ID, DEFAULT_CHANNEL_DESCRIPTION, NotificationManager.IMPORTANCE_DEFAULT
      )
      channel.enableVibration(true)
      manager.createNotificationChannel(channel)
    }
  }

  private fun getLauncherIntent(context: Context): Intent? {
    return context.packageManager.getLaunchIntentForPackage(context.packageName)?.run {
      setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
    }
  }

  data class Config(
    val notificationId: Int,
    val title: String,
    val message: String,
    @DrawableRes val icon: Int
  )
}
