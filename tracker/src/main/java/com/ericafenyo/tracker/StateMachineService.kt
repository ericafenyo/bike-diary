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
import android.util.Log
import com.ericafenyo.tracker.database.PreferenceDataStore
import com.ericafenyo.tracker.database.RecordCache
import com.ericafenyo.tracker.location.LocationUpdatesAction
import com.ericafenyo.tracker.logger.Logger
import com.google.gson.JsonObject
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.first
import org.json.JSONObject
import kotlin.coroutines.CoroutineContext

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

    // Exit if current

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
      }?.addOnFailureListener {
        Logger.error(this, tag, "Start location request unsuccessful: $it")
      }
    }
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
