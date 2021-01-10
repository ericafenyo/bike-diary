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

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.ericafenyo.bikediary.tracker.logger.Logger

class StateMachineReceiver : BroadcastReceiver() {

  override fun onReceive(context: Context, intent: Intent) {
    Logger.debug(context, TAG, "onReceive(Context $context, Intent $intent)")

    if (intent.action == context.getString(R.string.tracker_action_initialize)) {
      // TODO: 1/9/21
      //  1. Check for consent
      //  2. Check for location permission
      //  3. break if the above condition is not satisfied
    }

    // we should only get here if the user has consented
    // Start the State machine service
    val startIntent: Intent = StateMachineService.getStartIntent(context)
    startIntent.action = intent.action
    context.startService(startIntent)
  }


  companion object {
    private const val TAG = "StateMachineReceiver"
  }
}
