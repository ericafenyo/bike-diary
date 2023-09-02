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

package com.ericafenyo.tracker.timer

import java.util.concurrent.Executors
import java.util.concurrent.ScheduledExecutorService
import java.util.concurrent.TimeUnit
import timber.log.Timber

//class StopWatch private constructor() {
//  private var timer = Timer()
//  private var duration = Duration.ZERO
//
//  /**
//   *  The duration of time that has passed since the stopwatch was
//   *  last resumed until it was paused. That is, the total duration of time
//   *  that has passed while the stopwatch is running.
//   */
//  private var elapseDuration: Long = 0L
//
//  /** Represents the time when the stopwatch was initially started */
//  private var startTime: Long = 0L
//
//  /** Represents the time the stopwatch was paused */
//  private var stopTime: Long = 0L
//
//  /** Represents the time the when the stopwatch was last started  or resumed. */
//  private var currentTime: Long = 0L
//
//  private var onChangeListener: OnChangeListener? = null
//
//  fun setListener(onChangeListener: OnChangeListener) {
//    this.onChangeListener = onChangeListener
//  }
//
//  fun start() {
//    // Record the current time as the startTime when we start the stopwatch.
//    startTime = System.nanoTime()
//    currentTime = startTime
//    startTimer()
//  }
//
//  fun stop() {
//    pause()
//    duration = Duration.ZERO
//    onChangeListener?.onChange(0)
//  }
//
//  fun pause() {
//    stopTime = System.nanoTime()
//    // Get the duration of time passed since the stopwatch was
//    // last started or resumed until it was paused.
//    val elapsedSinceLastResume = stopTime - currentTime
//    elapseDuration += elapsedSinceLastResume
//    currentTime = stopTime
//    timer.cancel()
//
//    val time = this.getTime()
//    val (h,m,s) = getElapsedDurationInHMS()
//    Timber.tag("DEBUGGING_LOG").i("%02d : %02d : %02d", h, m, s)
//    Timber.tag("DEBUGGING_LOG").i(format(time))
//  }
//
//  fun resume() {
//    val elapsedTime = System.nanoTime() - stopTime
//    startTime -= elapsedTime
//    currentTime = startTime
//    stopTime = 0L
//    startTimer()
//  }
//
//  private fun startTimer() {
//    timer = fixedRateTimer(initialDelay = 1000L, period = 1000L) {
//      duration = duration.plus(1, ChronoUnit.SECONDS)
//      onChangeListener?.onChange(duration.toMillis())
//    }
//  }
//
//  fun getTime(): Long {
//    val currentTimeNs = System.nanoTime()
//    val timeSinceLastResume = currentTimeNs - currentTime
//    val totalTimeNs = elapseDuration + timeSinceLastResume
//
//    return TimeUnit.MILLISECONDS.convert(totalTimeNs, TimeUnit.NANOSECONDS)
//  }
//
//  fun getElapsedDuration(): Long {
//    if (startTime != 0L) {
//      return elapseDuration + (System.nanoTime() - currentTime)
//    }
//    return elapseDuration
//  }
//
//  fun getElapsedDurationInHMS(): Triple<Long, Long, Long> {
//    val elapsedNs = getElapsedDuration()
//    val elapsedSeconds = elapsedNs / 1_000_000_000
//    val elapsedMinutes = elapsedSeconds / 60
//    val elapsedHours = elapsedMinutes / 60
//
//    val remainingMinutes = elapsedMinutes % 60
//    val remainingSeconds = elapsedSeconds % 60
//
//    return Triple(elapsedHours, remainingMinutes, remainingSeconds)
//  }
//
//  fun format(time: Long): String {
//    var hours: Long = 0
//    var minutes: Long = 0
//    var seconds: Long = 0
//    var milliseconds: Long = time
//
//    hours = milliseconds / MILLIS_PER_HOUR
//    milliseconds %= MILLIS_PER_HOUR
//
//    minutes = milliseconds / MILLIS_PER_MINUTE
//    milliseconds %= MILLIS_PER_MINUTE
//
//    seconds = milliseconds / MILLIS_PER_SECOND
//
//    return String.format("%02d : %02d : %02d", hours, minutes, seconds)
//  }
//
//  fun interface OnChangeListener {
//    fun onChange(milliseconds: Long)
//  }
//
//  companion object {
//    internal const val NANOS_PER_MILLIS: Long = 1000000
//
//    /**
//     * Number of milliseconds in a second.
//     */
//    const val MILLIS_PER_SECOND: Long = 1000
//
//    /**
//     * Number of milliseconds in a standard minute.
//     */
//    const val MILLIS_PER_MINUTE: Long = 60 * MILLIS_PER_SECOND
//
//    /**
//     * Number of milliseconds in a standard hour.
//     */
//    const val MILLIS_PER_HOUR: Long = 60 * MILLIS_PER_MINUTE
//
//
//    @Volatile
//    private var INSTANCE: StopWatch? = null
//
//    @JvmStatic
//    fun getInstance(): StopWatch {
//      return INSTANCE ?: synchronized(this) {
//        INSTANCE ?: StopWatch()
//          .also { INSTANCE = it }
//      }
//    }
//  }
//}


class StopWatch private constructor() {
  private var timerExecutor: ScheduledExecutorService? = null
  private var isRunning = false
  private var elapsedNs: Long = 0L
  private var startTimeNs: Long = 0L

  private var onChangeListener: OnChangeListener? = null

  fun setListener(onChangeListener: OnChangeListener) {
    this.onChangeListener = onChangeListener
  }

  fun start() {
    if (!isRunning) {
      startTimeNs = System.nanoTime()
      isRunning = true
      startTimer()
    }
  }

  fun stop() {
    if (isRunning) {
      isRunning = false
      timerExecutor?.shutdownNow()
      elapsedNs = 0L
      onChangeListener?.onChange(0)
    }
  }

  fun pause() {
    if (isRunning) {
      val currentTimeNs = System.nanoTime()
      elapsedNs += currentTimeNs - startTimeNs
      isRunning = false
      timerExecutor?.shutdownNow()
      onChangeListener?.onChange(getTime())
      val time = getTime()
      Timber.tag("DEBUGGING_LOG").i(format(time))
    }
  }

  fun resume() {
    if (!isRunning) {
      startTimeNs = System.nanoTime()
      isRunning = true
      startTimer()
    }
  }

  private fun startTimer() {
    timerExecutor = Executors.newSingleThreadScheduledExecutor()
    timerExecutor?.scheduleAtFixedRate(
      {
        if (isRunning) {
          val time = getTime()
          onChangeListener?.onChange(time)
        }
      },
      0, 1000, TimeUnit.MILLISECONDS
    )
  }

  fun getTime(): Long {
    val currentTimeNs = System.nanoTime()
    val totalElapsedNs = elapsedNs + if (isRunning) currentTimeNs - startTimeNs else 0L
    return TimeUnit.MILLISECONDS.convert(totalElapsedNs, TimeUnit.NANOSECONDS)
  }

  fun format(time: Long): String {
    var hours: Long = 0
    var minutes: Long = 0
    var seconds: Long = 0
    var milliseconds: Long = time

    hours = milliseconds / MILLIS_PER_HOUR
    milliseconds %= MILLIS_PER_HOUR

    minutes = milliseconds / MILLIS_PER_MINUTE
    milliseconds %= MILLIS_PER_MINUTE

    seconds = milliseconds / MILLIS_PER_SECOND

    return String.format("%02d : %02d : %02d", hours, minutes, seconds)
  }

  fun interface OnChangeListener {
    fun onChange(milliseconds: Long)
  }

  companion object {
    /**
     * Number of milliseconds in a second.
     */
    const val MILLIS_PER_SECOND: Long = 1000

    /**
     * Number of milliseconds in a standard minute.
     */
    const val MILLIS_PER_MINUTE: Long = 60 * MILLIS_PER_SECOND

    /**
     * Number of milliseconds in a standard hour.
     */
    const val MILLIS_PER_HOUR: Long = 60 * MILLIS_PER_MINUTE


    @Volatile
    private var INSTANCE: StopWatch? = null

    @JvmStatic
    fun getInstance(): StopWatch {
      return INSTANCE ?: synchronized(this) {
        INSTANCE ?: StopWatch()
          .also { INSTANCE = it }
      }
    }
  }
}
