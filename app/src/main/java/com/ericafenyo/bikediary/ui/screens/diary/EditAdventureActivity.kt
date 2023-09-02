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

package com.ericafenyo.bikediary.ui.screens.diary

import android.content.Context
import android.content.Intent
import android.content.Intent.FLAG_ACTIVITY_NEW_TASK
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.ericafenyo.bikediary.OnTextChangedListener
import com.ericafenyo.bikediary.R
import com.ericafenyo.bikediary.databinding.ActivityEditAdventureBinding
import com.wada811.databinding.dataBinding
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

@AndroidEntryPoint
class EditAdventureActivity : AppCompatActivity() {
  private val binding: ActivityEditAdventureBinding by dataBinding()
  private val adventureModel: EditAdventureViewModel by viewModels()

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_edit_adventure)

    binding.lifecycleOwner = this
    binding.model = adventureModel

    binding.onTextChangedListener = object : OnTextChangedListener {
      override fun onTextChanged(view: View, text: String) {
        Timber.tag("EditAdventureActivity").d("onTextChanged: view: $view text: $text")
      }
    }

    binding.toolbar.apply {
      inflateMenu(R.menu.save)
      val saveItem = menu.findItem(R.id.action_save) ?: return
      saveItem.setOnMenuItemClickListener {
        val title = binding.editTextAdventureTitle.text.toString()
        val note = binding.editTextAdventureNote.text.toString()
        finish()
        true
      }
    }
  }

  companion object {
    /**
     * Creates an intent with arguments for starting this activity
     * @param packageContext the context we are navigating from
     *
     * @return an [Intent] with an extra information
     */
    fun getStartIntent(packageContext: Context): Intent {
      return Intent(packageContext, EditAdventureActivity::class.java).run {
        addFlags(FLAG_ACTIVITY_NEW_TASK)
      }
    }
  }
}
