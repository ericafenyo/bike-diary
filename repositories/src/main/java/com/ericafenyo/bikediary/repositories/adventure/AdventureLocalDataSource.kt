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

package com.ericafenyo.bikediary.repositories.adventure

import com.ericafenyo.bikediary.database.AppDatabase
import com.ericafenyo.bikediary.database.entity.AdventureEntity
import com.ericafenyo.bikediary.model.Adventure
import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

@Singleton
class AdventureLocalDataSource @Inject constructor(database: AppDatabase) {
  private val dao = database.getAdventureDao()

  fun adventure(uuid: String): Flow<Adventure> = dao.adventure(uuid).map { entity ->
    entity.toAdventure()
  }

  fun adventures(): Flow<List<Adventure>> = dao.adventures().map { entities ->
    entities.map { entity -> entity.toAdventure() }
  }

  suspend fun getUnprocessedAdventures(): List<Adventure> = dao.getUnprocessedAdventures()
    .map { entity -> entity.toAdventure() }

  suspend fun insertAll(adventures: List<Adventure>) = dao.bulkInsert(
    adventures.map { entity -> entity.toEntity() }
  )

  private fun Adventure.toEntity() = AdventureEntity(
    id = id,
    title = title,
    speed = speed,
    duration = duration,
    distance = distance,
    calories = calories,
    startTime = startTime,
    endTime = endTime,
    imagePath = image,
  )

  private fun AdventureEntity.toAdventure() = Adventure(
    id = id,
    title = title,
    speed = speed,
    duration = duration,
    distance = distance,
    calories = calories,
    startTime = startTime,
    endTime = endTime,
    image = imagePath,
  )
}
