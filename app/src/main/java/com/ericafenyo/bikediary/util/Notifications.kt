/*
 * The MIT License (MIT)
 *
 * Copyright (C) 2023 Eric Afenyo
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package com.ericafenyo.bikediary.util

import android.annotation.TargetApi
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.annotation.DrawableRes
import androidx.core.app.NotificationCompat
import com.ericafenyo.bikediary.R

object Notifications {
  private const val TAG = "Notifications"
  private const val NOTIFICATION_CHANNEL_ID = "tracker_notification_channel"
  private const val NOTIFICATION_CHANNEL_DESCRIPTION = "Notification channel used for the tracker"

  fun createTrackerNotification(context: Context): Notification {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
      createNotificationChannel(context)
    }

    val notification = NotificationCompat.Builder(context, NOTIFICATION_CHANNEL_ID)
      .setSmallIcon(R.drawable.ic_bike)
      .setContentTitle(context.getString(R.string.app_name))
      .setContentText(context.getString(R.string.tracking_active))
      // Starting from SDK version 5.0.0, it is recommended to use a low priority notification
      .setPriority(NotificationCompat.PRIORITY_LOW)
      .setContentIntent(defaultPendingIntent(context))
      .build()
    notification.flags = Notification.FLAG_ONGOING_EVENT
    return notification
  }

  private fun defaultPendingIntent(context: Context): PendingIntent = PendingIntent.getActivity(
    context,
    0,
    getLauncherIntent(context),
    PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
  )

  fun cancel(context: Context, notificationId: Int) {
    val manager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
    com.ericafenyo.bikediary.logger.Logger.debug(
      context,
      TAG,
      "Cancelling notify with id $notificationId"
    )
    manager.cancel(notificationId)
  }

  @TargetApi(26)
  private fun createNotificationChannel(context: Context) {
    // Only call on Android O and above
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
      val manager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
      for (chanel in manager.notificationChannels) {
        com.ericafenyo.bikediary.logger.Logger.debug(
          context,
          TAG,
          "Checking channel with id = ${chanel.id}"
        )
        if (NOTIFICATION_CHANNEL_ID == chanel.id) {
          com.ericafenyo.bikediary.logger.Logger.debug(
            context,
            TAG,
            "Default channel found, returning"
          )
          return
        }
      }
      com.ericafenyo.bikediary.logger.Logger.debug(
        context,
        TAG,
        "Default channel not found, creating a new one"
      )
      val channel = NotificationChannel(
        NOTIFICATION_CHANNEL_ID,
        NOTIFICATION_CHANNEL_DESCRIPTION,
        NotificationManager.IMPORTANCE_DEFAULT
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
    @DrawableRes val icon: Int = R.drawable.ic_bike,
    val cancellable: Boolean = true,
  )
}
