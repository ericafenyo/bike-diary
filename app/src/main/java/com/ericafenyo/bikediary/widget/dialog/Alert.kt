/*
 * The MIT License (MIT)
 *
 * Copyright (C) 2021 TransWay
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

package com.ericafenyo.bikediary.widget.dialog

import android.content.Context
import android.view.View
import androidx.annotation.StringRes
import com.ericafenyo.bikediary.R
import com.ericafenyo.bikediary.databinding.DialogAlertBinding
import com.wada811.databinding.dataBinding

/**
 * An android AlertDialog with a custom view.
 */
class Alert private constructor(
  private val title: String,
  private val message: String,
  private val action: String,
  private val block: (() -> Unit)? = null,
) : AbstractDialogFragment(R.layout.dialog_alert) {
  private val binding: DialogAlertBinding by dataBinding()

  override fun onCreated(view: View) {
    binding.textTitle.text = title
    binding.textMessage.text = message
    binding.button.text = action
    binding.button.setOnClickListener {
      if (block != null) block.invoke() else requireDialog().dismiss()
    }
  }

  class Builder(private val context: Context) {
    private var _title: String = ""
    private var _message: String = ""
    private var _action: String = ""
    private var _block: (() -> Unit)? = null

    /**
     * Set the alert properties from the given [message] object.
     *
     * @return this [Alert.Builder] object to allow for chaining of calls to set methods
     */
    fun from(message: Message): Builder {
      this._title = context.getString(message.titleId)
      this._message = context.getString(message.messageId)
      this._action = context.getString(message.actionId)
      return this
    }

    /**
     * Set the title to display using the given resource id.
     *
     * @return this [Alert.Builder] object to allow for chaining of calls to set methods
     */
    fun setTitle(@StringRes titleId: Int): Builder {
      this._title = context.getString(titleId)
      return this
    }

    /**
     * Set the message to display using the given resource id.
     *
     * @return this [Alert.Builder] object to allow for chaining of calls to set methods
     */
    fun setMessage(@StringRes messageId: Int): Builder {
      this._message = context.getString(messageId)
      return this
    }

    /**
     * Set text to display in the button using the given resource id.
     *
     * @return this [Alert.Builder] object to allow for chaining of calls to set methods
     */
    fun setActionText(@StringRes actionId: Int): Builder {
      this._action = context.getString(actionId)
      return this
    }

    fun onAction(block: (() -> Unit)? = null): Builder {
      this._block = block
      return this
    }

    fun build(): Alert = Alert(_title, _message, _action, _block)
  }


  /**
   * A data class containing properties for creating an [Alert]
   *
   * @property titleId Optional string resource id of the title to show
   * @property messageId String resource id of the message to show
   * @property actionId Optional string resource id of the button label
   * @property closable Optional make the dialog non dismissible set to false
   */
  data class Message(
    @StringRes val messageId: Int,
    @StringRes val titleId: Int,
    @StringRes val actionId: Int = android.R.string.ok,
    val closable: Boolean = true,
  )
}
