/*
 * The MIT License (MIT)
 *
 * Copyright (C) 2020 Eric Afenyo
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

package com.ericafenyo.habitdiary

import android.content.Context
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.appcompat.content.res.AppCompatResources
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.LifecycleOwner
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.target.Target
import com.bumptech.glide.request.transition.Transition
import com.ericafenyo.habitdiary.databinding.FragmentProfileBinding
import com.ericafenyo.habitdiary.ui.MainActivityViewModel
import com.wada811.databinding.dataBinding
import dagger.hilt.android.AndroidEntryPoint

/**
 * Display the user's profile information
 *
 * @author Eric
 * @since 1.0
 *
 * created on 2020-11-09
 */
@AndroidEntryPoint
class ProfileFragment : Fragment(R.layout.fragment_profile) {
  private val binding: FragmentProfileBinding by dataBinding()
  private val activityModel: MainActivityViewModel by viewModels()

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)

    binding.toolbar.setupProfileAvatar(activityModel, this)
  }
}

fun Toolbar.setupProfileAvatar(
  viewModel: MainActivityViewModel,
  lifecycleOwner: LifecycleOwner
) {
  inflateMenu(R.menu.avatar)
  val profileItem = menu.findItem(R.id.action_profile) ?: return
  profileItem.setOnMenuItemClickListener {
    viewModel.onProfileClicked()
    true
  }

  val avatarSize = resources.getDimensionPixelSize(R.dimen.nav_avatar_size)
  val target = profileItem.asGlideTarget(avatarSize)
  viewModel.currentUserImageUri.observe(lifecycleOwner, {
    setProfileAvatar(context, target, it)
  })
}

fun MenuItem.asGlideTarget(size: Int): Target<Drawable> = object : CustomTarget<Drawable>(size, size) {

    override fun onLoadStarted(placeholder: Drawable?) {
      icon = placeholder
    }

    override fun onLoadFailed(errorDrawable: Drawable?) {
      icon = errorDrawable
    }

    override fun onLoadCleared(placeholder: Drawable?) {
      icon = placeholder
    }

    override fun onResourceReady(resource: Drawable, transition: Transition<in Drawable>?) {
      icon = resource
    }
  }

fun setProfileAvatar(
  context: Context,
  target: Target<Drawable>,
  imageUri: Uri?,
  placeholder: Int = R.drawable.ic_default_profile_avatar
) {
  // Inflate the drawable for proper tinting
  val placeholderDrawable = AppCompatResources.getDrawable(context, placeholder)
  when (imageUri) {
    null -> {
      Glide.with(context)
        .load(placeholderDrawable)
        .apply(RequestOptions.circleCropTransform())
        .into(target)
    }
    else -> {
      Glide.with(context)
        .load(imageUri)
        .apply(RequestOptions.placeholderOf(placeholderDrawable).circleCrop())
        .into(target)
    }
  }
}
