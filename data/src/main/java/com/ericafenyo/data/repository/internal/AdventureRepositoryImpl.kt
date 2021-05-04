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

package com.ericafenyo.data.repository.internal

import android.util.Log
import com.ericafenyo.data.api.BikeDiaryService
import com.ericafenyo.data.database.AdventureEntity
import com.ericafenyo.data.model.Adventure
import com.ericafenyo.data.repository.AdventureRepository
import com.ericafenyo.tracker.datastore.DataStoreModel
import com.ericafenyo.tracker.util.JSON
import java.util.*
import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

@Singleton
@OptIn(ExperimentalCoroutinesApi::class)
class AdventureRepositoryImpl @Inject constructor(
  private val service: BikeDiaryService,
  private val mapper: AdventureMapper,
  private val trackerDataSource: DataStoreModel,
  private val localStore: AdventureLocalDataSource,
) : AdventureRepository {
  override suspend fun getAdventures(): Flow<List<Adventure>> = flow {
    Log.d("Tag", "Trying to get adventures")
    val adventures = localStore.getAdventures();
    Log.d("Tag", "Trying to get adventures $adventures")
    emit(toEntities(adventures))
  }

  private suspend fun toEntities(adventures: List<AdventureEntity>): List<Adventure> {
    return adventures.map { adventure -> mapper.map(adventure) }
  }

  override suspend fun draftAdventures(): String {
    val record = trackerDataSource.getRecord()
    if (record != null) {
      val stringData = record.data
      val valueObject = JSON.parse(stringData, TrackerVO::class)

      val data = record.data
      val metadata = valueObject.metadata


      val adventure = AdventureEntity(
        id = "Unprocessed-${UUID.randomUUID()}",
        title = "Untitled adventure",
        speed = metadata.speed,
        duration = metadata.duration,
        distance = metadata.distance,
        calories = metadata.calories,
        date = "metadata.date",
        geojson = JSON.stringify(data),
        staticMap = ""
      )
        localStore.save(adventure)
      Log.d("Log Tag", "Record from local store $adventure")
    }
    return ""
  }
}

data class TrackerVO(
  val data: Any,
  val metadata: Metadata,
) {
  data class Metadata(
    val id: String = "Unprocessed-${UUID.randomUUID()}",
    val title: String = "",
    val speed: Double,
    val duration: Double,
    val distance: Double,
    val calories: Double,
    //val date: String,
  )
}
