package model

import android.content.Context
import android.content.Intent
import androidx.annotation.StringRes

/**
 * Create an explicit intent that sets the package name correctly and converts it to an implicit intent.
 */
class ExplicitIntent(
  context: Context, @StringRes actionId: Int
) : Intent(context.getString(actionId)) {
  init {
    setPackage(context.packageName)
  }
}

fun Context.getExplicitIntent(@StringRes actionId: Int) = ExplicitIntent(this, actionId)