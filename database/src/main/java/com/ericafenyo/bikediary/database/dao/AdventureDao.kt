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

package com.ericafenyo.bikediary.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.ericafenyo.bikediary.database.entity.AdventureEntity
import kotlinx.coroutines.flow.Flow

/**
 * A Data Access Object with variety of query methods for database interactions.
 *
 * @author Eric
 * @since 1.0
 *
 * created on 2021-04-17
 */
@Dao
interface AdventureDao {
  @Insert(onConflict = OnConflictStrategy.REPLACE)
  suspend fun insert(adventure: AdventureEntity)

  @Query("SELECT * FROM adventures WHERE id = :adventureId")
  suspend fun getAdventureById(adventureId: String): AdventureEntity

  @Query("SELECT * FROM adventures")
  suspend fun getAdventures(): List<AdventureEntity>

  @Query("SELECT * FROM adventures ORDER BY id DESC Limit 1")
  fun adventure(): Flow<AdventureEntity>

  @Query("DELETE FROM adventures WHERE id = :adventureId")
  suspend fun deleteById(adventureId: String): Int

  @Query("DELETE FROM adventures")
  suspend fun clearAll(): Int
}
