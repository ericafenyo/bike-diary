//package com.ericafenyo.tracker.util
//
//import android.app.Notification
//import android.app.NotificationManager
//import android.app.PendingIntent
//import android.content.Context
//import android.content.Intent
//import android.graphics.Bitmap
//import android.os.Build
//import android.util.Log
//import androidx.core.app.NotificationCompat
//import androidx.core.app.NotificationManagerCompat
//import com.ericafenyo.tracker.logger.Logger
//
//
//class GlobalNotificationBuilder() {
//  companion object {
//    @Volatile
//    private var INSTANCE: NotificationCompat.Builder? = null
//
////    @JvmStatic
////    fun getInstance(): GlobalNotificationBuilder {
////      return INSTANCE ?: synchronized(this) {
////        INSTANCE ?: GlobalNotificationBuilder()
////          .also { INSTANCE = it }
////      }
////    }
//  }
//}
//
//data class NotificationConfig(
//  val notificationId: Int,
//  val title: String,
//  val message: String,
//)
//
//object NotificationUtil {
//  private const val TAG = "Notification"
//  private const val DEFAULT_CHANNEL_ID = "TrackerNotificationChannel"
//  private const val DEFAULT_CHANNEL_DESCRIPTION = "Notification channel used the tracker"
//
//  fun createNotification(context: Context, config: NotificationConfig) {
//    val (notificationId, title, message) = config
//
//    val notificationStyle = NotificationCompat.BigTextStyle()
//      .bigText(title)
//      .setBigContentTitle(message)
//
//    val activityIntent = getLauncherIntent(context)
//    val activityPendingIntent = PendingIntent.getActivity(
//      context, 0, activityIntent, PendingIntent.FLAG_UPDATE_CURRENT
//    )
//
//    val appIcon: Bitmap = BitmapFactory.decodeResource(context.resources, R.drawable.ic_visibility_black)
//    builder.setLargeIcon(appIcon)
//    builder.setSmallIcon(R.drawable.ic_visibility_black)
//    //builder.setContentTitle(context.getString(R.string.app_name));
//    builder.setContentText(message)
//    return builder
//
//    Logger.debug(
//      context, TAG, "Generating notify with id $notificationId and message $message"
//    )
//
//    getNotificationBuilder(context)
//      .setContentIntent(activityPendingIntent)
//      .build()
//      .also { NotificationManagerCompat.from(context).notify(notificationId, it) }
//  }
//
//  fun cancelNotification(context: Context, notificationId: Int) {
//    val manager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
//    Logger.debug(context, TAG, "Cancelling notify with id $notificationId")
//    manager.cancel(notificationId)
//  }
//
//  private fun getNotificationBuilder(context: Context): NotificationCompat.Builder {
//    val manager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
//    createNotificationChannel(context)
//    return NotificationCompat.Builder(context, DEFAULT_CHANNEL_ID)
//  }
//
//  @TargetApi(26)
//  private fun createNotificationChannel(ctxt: Context) {
//    // only call on Android O and above
//    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//      val notificationManager: NotificationManager =
//        ctxt.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
//      val channels: List<NotificationChannel> = notificationManager.getNotificationChannels()
//      for (i in channels.indices) {
//        val id: String = channels[i].getId()
//        Log.d(ctxt, TAG, "Checking channel with id = $id")
//        if (DEFAULT_CHANNEL_ID == id) {
//          Log.d(ctxt, TAG, "Default channel found, returning")
//          return
//        }
//      }
//      Log.d(ctxt, TAG, "Default channel not found, creating a new one")
//      val dChannel = NotificationChannel(
//        DEFAULT_CHANNEL_ID,
//        DEFAULT_CHANNEL_DESCRIPTION, NotificationManager.IMPORTANCE_LOW
//      )
//      dChannel.enableVibration(true)
//      notificationManager.createNotificationChannel(dChannel)
//    }
//  }
//
//  private fun getLauncherIntent(context: Context): Intent? {
//    return context.packageManager.getLaunchIntentForPackage(context.packageName)?.run {
//      setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
//    }
//  }
//}
