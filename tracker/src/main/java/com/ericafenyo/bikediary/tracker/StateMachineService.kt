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

package com.ericafenyo.bikediary.tracker

import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.IBinder
import com.ericafenyo.bikediary.tracker.database.PreferenceDataStore
import com.ericafenyo.bikediary.tracker.logger.Logger
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.runBlocking

class StateMachineService : Service() {
  private val tag = "StateMachineService"
  private val ioContext = Dispatchers.IO

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
    runBlocking(ioContext) {
      PreferenceDataStore.getInstance(this@StateMachineService)
        .getString(
          getString(R.string.tracker_current_state_key),
          getString(R.string.tracker_state_start)
        )
        .collect { currentState ->
          Logger.debug(this@StateMachineService, tag, "The current state is $currentState")
          // 1. TODO: 1/10/21 Save action to database
          handleAction(currentState, intent.action)
        }
    }


    // Return START_REDELIVER_INTENT to scheduled for a restart with the same Intent
    // if the service's process is killed while it is started.
    // https://developer.android.com/reference/android/app/Service#START_REDELIVER_INTENT
    return START_REDELIVER_INTENT
  }

  private fun handleAction(currentState: String, action: String?) {
    Logger.debug(this, tag, "handleAction(currentState: $currentState , action: $action)")
    if (action == null) {
      Logger.debug(this, tag, "action is null, exiting")
      return
    }

    when (action) {
      getString(R.string.tracker_action_initialize) -> handleInitialize(currentState, action)
    }
  }

  private fun handleInitialize(currentState: String, action: String) {
    Logger.debug(this, tag, "handleInitialize(currentState: $currentState, action: $action)")

    // Get current location

    // Exit quickly if tracking is disabled
    exitOnTrackingDisabled(currentState)
    setNewState(this, getString(R.string.tracker_state_ready), false)
  }

  private fun exitOnTrackingDisabled(currentState: String) {
    if (currentState == getString(R.string.tracker_state_disabled)) {
      Logger.debug(this, tag, "Tracking is disabled, exiting")
      return
    }
  }

  fun setNewState(context: Context, newState: String, doChecks: Boolean) {
    Logger.debug(context, tag, "New state after handling action is $newState")

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