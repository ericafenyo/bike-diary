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

package com.ericafenyo.tracker.database.record

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.ericafenyo.tracker.database.record.Record
import kotlinx.coroutines.flow.Flow

/**
 * Data access object for the [Record] entity
 * @author Eric
 * @since 1.0
 *
 * created on 2021-01-30
 */
@Dao
interface RecordDao {
  @Insert(onConflict = OnConflictStrategy.REPLACE)
  suspend fun insert(record: Record)

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  suspend fun bulkInsert(recodes: List<Record>)

  @Query("SELECT * FROM records ORDER BY id ASC")
  suspend fun getAll(): List<Record>

  @Query("SELECT * FROM records ORDER BY id ASC")
  fun streams(): Flow<List<Record>>

  @Query("SELECT * FROM records ORDER BY id DESC Limit 1")
  suspend fun getLatest(): Record

  @Query("SELECT * FROM records ORDER BY id DESC Limit 1")
  fun single(): Flow<Record>

  @Query("DELETE FROM records")
  suspend fun clear()
}
