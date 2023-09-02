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

import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.IBinder
import androidx.work.WorkManager
import com.ericafenyo.bikediary.di.qualifier.ApplicationScope
import com.ericafenyo.bikediary.logger.Logger
import com.ericafenyo.tracker.Tracker.State
import com.ericafenyo.tracker.Tracker.State.IDLE
import com.ericafenyo.tracker.Tracker.State.ONGOING
import com.ericafenyo.tracker.Tracker.State.PAUSE
import com.ericafenyo.tracker.analysis.worker.AnalysisWorker
import com.ericafenyo.tracker.database.record.RecordCache
import com.ericafenyo.tracker.location.LocationUpdatesAction
import com.ericafenyo.tracker.timer.StopWatch
import com.ericafenyo.tracker.util.PermissionsManager
import dagger.hilt.android.AndroidEntryPoint
import java.time.Duration
import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import timber.log.Timber

@Singleton
@AndroidEntryPoint
class ActionProcessor : Service() {
  private val tracker = Tracker.getInstance()
  @Inject lateinit var coroutineScope: CoroutineScope
  val watch = StopWatch.getInstance()

  override fun onBind(intent: Intent): IBinder? {
    return null
  }

  override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
    Logger.debug(this, TAG, "onStartCommand(intent: $intent,  flags: $flags, startId: $startId)")

    if (flags == START_FLAG_REDELIVERY) {
      // Service restated after being killed before calling stopSelf(int) for the given Intent.
      // https://developer.android.com/reference/android/app/Service#START_FLAG_REDELIVERY
      Logger.debug(this, TAG, "Service restated after being killed!")
    }

    // Retrieve the current state of the tracker
    val currentState = tracker.getState()
    Logger.debug(applicationContext, TAG, "The current tracker state is $currentState")

    handleAction(currentState, intent.action)

    // Return START_REDELIVER_INTENT to scheduled for a restart with the same Intent
    // if the service's process is killed while it is started.
    // https://developer.android.com/reference/android/app/Service#START_REDELIVER_INTENT
    return START_REDELIVER_INTENT
  }

  override fun onDestroy() {
    super.onDestroy()
    Logger.debug(applicationContext, TAG, "onDestroy()")
  }

  private fun handleAction(currentState: State, action: String?) {
    Logger.debug(this, TAG, "handleAction(currentState: $currentState , action: $action)")
    if (action == null) {
      Logger.debug(this, TAG, "action is null, exiting")
      return
    }

    when (action) {
      getString(R.string.tracker_action_start) -> handleStart(currentState, action)
      getString(R.string.tracker_action_stop) -> handleStop(currentState, action)
      getString(R.string.tracker_action_pause) -> handlePause(currentState, action)
      getString(R.string.tracker_action_resume) -> handleResume(currentState, action)
    }
  }

  private fun handlePause(currentState: State, action: String) {
    Logger.debug(this, TAG, "handlePause(currentState: $currentState, action: $action)")
    // Only pause tracking if the state is ongoing.
    if (currentState == ONGOING) {
      // Stop the location-updates request
      LocationUpdatesAction(this).stop()?.addOnSuccessListener {
        Logger.error(this, TAG, "Stop location update request successful: $it")
        // The request is successful, change the current state to idle
        // We should now get location updates at a particular interval
        updateTrackerState(this, currentState, PAUSE)
        watch.pause()
//      com.ericafenyo.bikediary.util.Notifications.update(this, ONGOING_NOTIFICATION_ID)
      }?.addOnFailureListener {
        Logger.error(this, TAG, "Stop Location updates request unsuccessful: $it")
      }
    }
  }

  private fun handleResume(currentState: State, action: String) {
    Logger.debug(this, TAG, "handleResume(currentState: $currentState, action: $action)")
    // Only resume tracking if the state is paused.
    if (currentState == PAUSE) {
      // Start/Resume the location-updates request
      LocationUpdatesAction(this).start()?.addOnSuccessListener {
        Logger.debug(this, TAG, "Resume location update request successful")
        watch.resume()
        updateTrackerState(this, currentState, ONGOING)
//        NotificationManagerCompat.from(applicationContext).notify(6454653, options.notification)
      }?.addOnFailureListener { exception ->
        Logger.error(this, TAG, "Resume location update request unsuccessful: $exception")
      }
    }
  }

  private fun handleStart(currentState: State, action: String) {
    Logger.debug(this, TAG, "handleStart(currentState: $currentState, action: $action)")

    // Only start tracking if the state is idle.
    if (currentState == IDLE) {
      // Start the location-updates request
      LocationUpdatesAction(this).start()?.addOnSuccessListener {
        Logger.debug(this, TAG, "Start location update request successful")
        coroutineScope.launch {
          // Clean the cache
          RecordCache.getInstance(applicationContext).clear()
          watch.setListener { duration ->
            Timber.tag("DEBUGGING_LOG").i("${Duration.ofNanos(duration)}")
          }
          watch.start()
          // Change the current state to ongoing
          // We should now get location updates at a particular interval
          updateTrackerState(applicationContext, currentState, ONGOING)
//        NotificationManagerCompat.from(applicationContext).notify(6454653, options.notification)
        }
      }?.addOnFailureListener { exception ->
        Logger.error(this, TAG, "Start location update request unsuccessful: $exception")
//        if (!PermissionsManager.isForegroundLocationPermissionGranted(this)) {
////          showNotificationToAppSettings()
//        }
      }
    }
  }

//  private fun showNotificationToAppSettings() {
//    val config = Config(
//      LOCATION_REQUIRED_NOTIFICATION_ID,
//      "Location permission required",
//      "Click here to enable it",
//      R.drawable.ic_bike
//    )
//
//    val permissionAction = Action(
//      R.drawable.ic_bike,
//      "Change Permissions",
//      appSettingsPendingIntent()
//    )
//
//    com.ericafenyo.bikediary.util.Notifications.createWithAction(this, config, permissionAction)
//  }

//  private fun appSettingsPendingIntent(): PendingIntent? {
//    val intent = Intent().run {
//      action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
//      addCategory(Intent.CATEGORY_DEFAULT)
//      data = Uri.parse("package:$packageName")
//      addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
//      addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY)
//      addFlags(Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS)
//    }
//    return PendingIntent.getActivity(
//      this,
//      0,
//      intent,
//      PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
//    )
//  }

  private fun handleStop(currentState: State, action: String) {
    Logger.debug(this, TAG, "handleStop(currentState: $currentState, action: $action)")
    // Stop the location-updates request
    LocationUpdatesAction(this).stop()?.addOnSuccessListener {
      Logger.error(this, TAG, "Stop location update request successful: $it")
      // The request is successful, change the current state to idle
      // We should now get location updates at a particular interval
      watch.stop()
      updateTrackerState(this, currentState, IDLE)

//      com.ericafenyo.bikediary.util.Notifications.cancel(this, ONGOING_NOTIFICATION_ID)

      // Start the trip analysis process
      WorkManager.getInstance(this).enqueue(AnalysisWorker.request)
    }?.addOnFailureListener {
      Logger.error(this, TAG, "Stop Location updates request unsuccessful: $it")
      Timber.tag(TAG).e(it, "Error")
    }
  }


  private fun exitOnEqualState(currentState: State, newState: State) {
    if (currentState == newState) {
      Logger.debug(this, TAG, "Already in the state: $newState, exiting")
      return
    }
  }

  private fun updateTrackerState(context: Context, currentState: State, newState: State) {
    // Exit quickly if current state is same as state ready
    exitOnEqualState(currentState, newState)

    coroutineScope.launch {
      val state = tracker.updateState(newState)
      Logger.debug(context, TAG, "New state after processing action is $state")
    }
    stopSelf()
  }

  companion object {
    private const val TAG = "StateProcessor"

    @JvmStatic
    fun getStartIntent(context: Context): Intent {
      return Intent(context, ActionProcessor::class.java)
    }
  }
}
