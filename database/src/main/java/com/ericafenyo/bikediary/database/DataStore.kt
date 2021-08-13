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

<<<<<<< HEAD:tracker/src/main/java/com/ericafenyo/tracker/datastore/DataStore.kt
package com.ericafenyo.tracker.datastore
=======
package com.ericafenyo.bikediary.database
>>>>>>> a0365d1 (Test :logger module build):database/src/main/java/com/ericafenyo/bikediary/database/DataStore.kt

import android.content.Context
import android.icu.text.AlphabeticIndex.Record
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
<<<<<<< HEAD:tracker/src/main/java/com/ericafenyo/tracker/datastore/DataStore.kt
import androidx.room.TypeConverters

@TypeConverters(Converters::class)
@Database(entities = [Record::class], version = 1, exportSchema = false)
internal abstract class DataStore : RoomDatabase() {
  abstract fun getRecords(): RecordDao

  companion object {
    private const val DATABASE_NAME = "com.ericafenyo.tracker.Cache"
=======
import com.ericafenyo.bikediary.database.dao.RecordDao

@Database(entities = [Record::class], version = 1, exportSchema = false)
abstract class DataStore : RoomDatabase() {

  abstract fun getRecords(): RecordDao

  companion object {
>>>>>>> a0365d1 (Test :logger module build):database/src/main/java/com/ericafenyo/bikediary/database/DataStore.kt

    @Volatile
    private var INSTANCE: DataStore? = null

    @JvmStatic
    fun getInstance(context: Context): DataStore {
      return INSTANCE ?: synchronized(this) {
        INSTANCE ?: createDatabase(context)
          .also { INSTANCE = it }
      }
    }

    private fun createDatabase(context: Context): DataStore {
      return Room.databaseBuilder(
        context,
        DataStore::class.java,
        "com.ericafenyo.tracker.DataStore"
      ).fallbackToDestructiveMigration()
        .build()
    }
  }
}