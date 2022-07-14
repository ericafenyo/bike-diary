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


package com.ericafenyo.tracker.database

import android.content.Context

import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.ericafenyo.tracker.database.analyzed.AnalyzedData
import com.ericafenyo.tracker.database.analyzed.AnalyzedDataDao
import com.ericafenyo.tracker.database.record.Record
import com.ericafenyo.tracker.database.record.RecordDao

@TypeConverters(
  SimpleLocationConverter::class,
  TracesConverter::class
)
@Database(
  version = 1,
  exportSchema = false,
  entities = [Record::class, AnalyzedData::class]
)
abstract class TrackerDatabase : RoomDatabase() {
  abstract fun getRecords(): RecordDao
  abstract fun getAnalyzedDataDao(): AnalyzedDataDao

  companion object {
    @Volatile
    private var INSTANCE: TrackerDatabase? = null

    @JvmStatic
    fun getInstance(context: Context): TrackerDatabase {
      return INSTANCE ?: synchronized(this) {
        INSTANCE ?: createDatabase(context)
          .also { INSTANCE = it }
      }
    }

    private fun createDatabase(context: Context): TrackerDatabase {
      return Room.databaseBuilder(
        context,
        TrackerDatabase::class.java,
        "com.ericafenyo.tracker.database.TrackerDatabase"
      ).fallbackToDestructiveMigration().build()
    }
  }
}
