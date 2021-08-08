/*
 * Copyright (C) 2020 Transway
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.ericafenyo.tracker.util

import android.Manifest.permission.ACCESS_FINE_LOCATION
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager.PERMISSION_GRANTED
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import java.util.*

/**
 * We extracted all the required runtime permissions to this class.
 * These includes:
 * - ACCESS_FINE_LOCATION
 */
class PermissionsManager(private val listener: OnPermissionResultListener) {

  interface OnPermissionResultListener {
    fun onExplanationNeeded(permissionsToExplain: List<String>)

    fun onPermissionResult(requestCode: Int, granted: Boolean)
  }

  companion object {
    private const val FOREGROUND_LOCATION_PERMISSION = ACCESS_FINE_LOCATION
    private const val FOREGROUND_LOCATION_REQUEST_CODE = 6577

    @JvmStatic
    fun isPermissionGranted(context: Context, permission: String): Boolean {
      return ContextCompat.checkSelfPermission(context, permission) == PERMISSION_GRANTED
    }

    @JvmStatic
    fun isForegroundLocationPermissionGranted(context: Context): Boolean {
      return isPermissionGranted(context, FOREGROUND_LOCATION_PERMISSION)
    }
  }


  fun requestForegroundLocationPermission(activity: Activity) {
    ActivityCompat.requestPermissions(
      activity,
      arrayOf(FOREGROUND_LOCATION_PERMISSION),
      FOREGROUND_LOCATION_REQUEST_CODE
    )
  }

  private fun requestPermissions(activity: Activity, permissions: Array<String>) {
    val permissionsToExplain = ArrayList<String>()
    for (permission in permissions) {
      if (ActivityCompat.shouldShowRequestPermissionRationale(activity, permission)) {
        permissionsToExplain.add(permission)
      }
    }
    if (permissionsToExplain.size > 0) {
      // The developer should show an explanation to the user asynchronously
      listener.onExplanationNeeded(permissionsToExplain)
    }
    ActivityCompat.requestPermissions(
      activity,
      permissions,
      FOREGROUND_LOCATION_REQUEST_CODE
    )
  }

  /**
   * You should call this method from your activity onRequestPermissionsResult.
   *
   * @param requestCode  The request code passed in requestPermissions(android.app.Activity, String[], int)
   * @param permissions  The requested permissions. Never null.
   * @param grantResults The grant results for the corresponding permissions which is either
   * PERMISSION_GRANTED or PERMISSION_DENIED. Never null.
   */
  fun onRequestPermissionsResult(
    requestCode: Int, permissions: Array<String>, grantResults: IntArray
  ) {
    val granted = grantResults.isNotEmpty() && grantResults[0] == PERMISSION_GRANTED
    listener.onPermissionResult(requestCode, granted)
  }
}
