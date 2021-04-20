/*
 * The MIT License (MIT)
 *
 * Copyright (C) 2021 Eric Afenyo
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

package com.ericafenyo.tracker

import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.IBinder
import android.provider.Settings
import android.util.Log
import androidx.core.app.NotificationCompat.Action
import com.ericafenyo.tracker.analysis.AnalysisJobIntentService
import com.ericafenyo.tracker.database.PreferenceDataStore
import com.ericafenyo.tracker.database.RecordCache
import com.ericafenyo.tracker.location.LocationUpdatesAction
import com.ericafenyo.tracker.logger.Logger
import com.ericafenyo.tracker.util.LOCATION_REQUIRED_NOTIFICATION_ID
import com.ericafenyo.tracker.util.Notifications
import com.ericafenyo.tracker.util.Notifications.Config
import com.ericafenyo.tracker.util.ONGOING_NOTIFICATION_ID
import com.ericafenyo.tracker.util.PermissionsManager
import com.google.gson.JsonObject
import kotlin.coroutines.CoroutineContext
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.first

class StateMachineService : Service(), CoroutineScope {
  private val tag = "StateMachineService"

  private val ioContext = Dispatchers.IO
  private val job = Job()

  override val coroutineContext: CoroutineContext = job + Dispatchers.Main

  override fun onBind(intent: Intent): IBinder? {
    return null
  }

  override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
    Logger.debug(this, tag, "onStartCommand(intent: $intent,  flags: $flags, startId: $startId)")

    if (flags == START_FLAG_REDELIVERY) {
      // Service restated after being killed before calling stopSelf(int) for the given Intent.
      // https://developer.android.com/reference/android/app/Service#START_FLAG_REDELIVERY
      Logger.debug(this, tag, "Service restarted! need to check idempotence!")
    }

    // TODO: 1/10/21 change runBlocking with a proper coroutine builder
    launch(ioContext) {
      val currentState = PreferenceDataStore.getInstance(this@StateMachineService)
        .getString(
          getString(R.string.tracker_current_state_key),
          getString(R.string.tracker_state_start)
        ).first()


      Logger.debug(this@StateMachineService, tag, "The current state is $currentState")
      // 1. TODO: 1/10/21 Save action to database
      handleAction(currentState, intent.action)
    }

    // Return START_REDELIVER_INTENT to scheduled for a restart with the same Intent
    // if the service's process is killed while it is started.
    // https://developer.android.com/reference/android/app/Service#START_REDELIVER_INTENT
    return START_REDELIVER_INTENT
  }

  override fun onDestroy() {
    super.onDestroy()
    job.cancel()
  }

  private fun handleAction(currentState: String, action: String?) {
    Logger.debug(this, tag, "handleAction(currentState: $currentState , action: $action)")
    if (action == null) {
      Logger.debug(this, tag, "action is null, exiting")
      return
    }

    when (action) {
      getString(R.string.tracker_action_initialize) -> handleInitialize(currentState, action)
      getString(R.string.tracker_action_start) -> handleStart(currentState, action)
      getString(R.string.tracker_action_stop) -> handleStop(currentState, action)
    }
  }

  private fun handleInitialize(currentState: String, action: String) {
    Logger.debug(this, tag, "handleInitialize(currentState: $currentState, action: $action)")
    // Exit quickly if tracking is disabled
    exitOnTrackingDisabled(currentState)
    setNewState(this, currentState, getString(R.string.tracker_state_ready))
  }

  private fun handleStart(currentState: String, action: String) {
    Logger.debug(this, tag, "handleStart(currentState: $currentState, action: $action)")

    // Exit quickly if tracking is disabled
    exitOnTrackingDisabled(currentState)


    // Only start tracking if the state machine is ready.
    if (currentState == getString(R.string.tracker_state_ready)) {
      // Start the location-updates request
      LocationUpdatesAction(this).start()?.addOnSuccessListener { request ->
        // On success, add trip start message

        runBlocking(Dispatchers.IO) {
          val actionObject = JsonObject()
          actionObject.addProperty("action", "Trip start")
          RecordCache.getInstance(this@StateMachineService).putMessage(
            RecordCache.KEY_TRIP_STARTED, actionObject
          )
        }

        //Change the current state to ongoing
        // We should now get location updates at a particular interval
        setNewState(this, currentState, getString(R.string.tracker_state_ongoing))

        val notificationConfig = Config(
          notificationId = ONGOING_NOTIFICATION_ID,
          message = getString(R.string.tracking_notification_content_text),
          title = getString(R.string.tracking_notification_title_text),
          icon = R.drawable.ic_bike,
          cancellable = false
        )

        Notifications.create(this, notificationConfig)
      }?.addOnFailureListener {
        Logger.error(this, tag, "Start location request unsuccessful: $it")
        if (!PermissionsManager.isForegroundLocationPermissionGranted(this)) {
          showNotificationToAppSettings()
        }
      }
    }
  }

  private fun showNotificationToAppSettings() {
    val config = Config(
      LOCATION_REQUIRED_NOTIFICATION_ID,
      "Location permission required",
      "Click here to enable it",
      R.drawable.ic_bike
    )

    val permissionAction = Action(
      R.drawable.ic_bike,
      "Change Permissions",
      appSettingsPendingIntent()
    )

    Notifications.createWithAction(this, config, permissionAction)
  }

  private fun appSettingsPendingIntent(): PendingIntent? {
    val intent = Intent().run {
      action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
      addCategory(Intent.CATEGORY_DEFAULT)
      data = Uri.parse("package:$packageName")
      addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
      addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY)
      addFlags(Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS)
    }
    return PendingIntent.getActivity(this, 0, intent, 0)
  }

  private fun handleStop(currentState: String, action: String) {
    Logger.debug(this, tag, "handleStop(currentState: $currentState, action: $action)")
    // Stop the location-updates request
    LocationUpdatesAction(this).stop()?.addOnSuccessListener {
      // On success, add trip stopped message
      runBlocking(Dispatchers.IO) {
        val actionObject = JsonObject()
        actionObject.addProperty("action", "Trip stop")
        RecordCache.getInstance(this@StateMachineService).putMessage(
          RecordCache.KEY_TRIP_STOPPED, actionObject
        )
      }
      // If the request is successful, change the current state to ongoing
      // We should now get location updates at a particular interval
      setNewState(this, currentState, getString(R.string.tracker_state_ready))
      Notifications.cancel(this, ONGOING_NOTIFICATION_ID)

      // Notify trip end so we can analyse and parse
      // Start the trip analysis process
      AnalysisJobIntentService.enqueueWork(this, Intent(this, AnalysisJobIntentService::class.java))
    }?.addOnFailureListener {
      Log.e(tag, "Error", it)
      Logger.error(this, tag, "Stop Location updates request unsuccessful: $it")
    }
  }

  private fun exitOnTrackingDisabled(currentState: String) {
    if (currentState == getString(R.string.tracker_state_disabled)) {
      Logger.debug(this, tag, "Tracking is disabled, exiting")
      return
    }
  }

  private fun exitOnEqualState(currentState: String, newState: String) {
    if (currentState == newState) {
      Logger.debug(this, tag, "Already in the state: $newState, exiting")
      return
    }
  }

  private fun setNewState(context: Context, currentState: String, newState: String) {
    Logger.debug(context, tag, "New state after handling action is $newState")

    //Exit quickly if current state is same as state ready
    exitOnEqualState(currentState, newState)

    // TODO: 1/10/21  exit if new state is same as current state
    runBlocking {
      PreferenceDataStore.getInstance(context)
        .putString(getString(R.string.tracker_current_state_key), newState)
      Logger.debug(context, tag, "New state saved to preference storage")
    }

    // TODO: 1/10/21 Check settings after setting state

    stopSelf()
  }

  companion object {

    @JvmStatic
    fun getStartIntent(context: Context): Intent {
      return Intent(context, StateMachineService::class.java)
    }
  }
}
