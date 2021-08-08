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

package com.ericafenyo.bikediary.data

import com.ericafenyo.bikediary.model.Trip
import com.ericafenyo.bikediary.model.User

val mockTrips = listOf(
  Trip(
    image = "https://cdn.dribbble.com/users/4601337/screenshots/15286257/media/77362a6f5567378c5315a9ec55892b68.png?compress=1&resize=1600x1200",
    title = "City water skyline building",
    speed = 2.0,
    duration = 0.0,
    distance = 0.0,
    calories = 400,
    date = "",
    owner = User(
      uid = "",
      name = "Daria Shevtsova",
      avatar = "https://images.pexels.com/photos/1078058/pexels-photo-1078058.jpeg?auto=compress&cs=tinysrgb&dpr=2&w=500"
    )
  ),
  Trip(
    image = "https://cdn.dribbble.com/users/1720296/screenshots/15285095/media/f6427830484757ba5d82e84f9c3519c5.png?compress=1&resize=1600x1200",
    title = "City traffic street building",
    speed = 2.0,
    duration = 0.0,
    distance = 0.0,
    calories = 400,
    date = "",
    owner = User(
      uid = "",
      name = "Italo Melo",
      avatar = "https://images.pexels.com/photos/2379005/pexels-photo-2379005.jpeg?auto=compress&cs=tinysrgb&dpr=2&w=500"
    )
  ),
  Trip(
    image = "https://cdn.dribbble.com/users/60166/screenshots/15284776/media/6188f1c74b968fc0279d9f691b8ed072.jpg?compress=1&resize=1600x1200",
    title = "Santorini Greece",
    speed = 2.0,
    duration = 0.0,
    distance = 0.0,
    calories = 400,
    date = "",
    owner = User(
      uid = "",
      name = "Wilson Vitorino",
      avatar = "https://images.pexels.com/photos/2167673/pexels-photo-2167673.jpeg?auto=compress&cs=tinysrgb&dpr=2&w=500"
    )
  )
)