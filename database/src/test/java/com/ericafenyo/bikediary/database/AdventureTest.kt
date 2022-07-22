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

package com.ericafenyo.bikediary.database

import com.ericafenyo.bikediary.database.dao.AdventureDao
import com.google.common.truth.Truth.assertThat
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.UninstallModules
import java.util.UUID
import javax.inject.Inject
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.After
import org.junit.Before
import org.junit.Test

@HiltAndroidTest
@UninstallModules(DatabaseModule::class)
@OptIn(ExperimentalCoroutinesApi::class)
class AdventureTest : DatabaseTest() {
  @Inject lateinit var database: AppDatabase
  private lateinit var adventureDao: AdventureDao

  private val adventureId = "611947c1cd3f6222c84c4966"
  private val adventureUuid = "ac42f8ee-c3a3-4a1d-b88d-57b643483898"
  private val adventure = TestDatabaseUtils.createAdventure(adventureId)

  @Before
  fun setup() {
    hiltRule.inject()
    adventureDao = database.getAdventureDao()
  }

  @Test
  fun insert() {
    testScope.runBlockingTest {
      // Given that we insert an adventure
      adventureDao.insert(adventure)

      // When we access the inserted Adventure
      val savedAdventure = adventureDao.getAdventure(adventureId)

      // Then it must match the local variable 'adventureId'.
      assertThat(savedAdventure.id).isEqualTo(adventureId)
    }
  }

  @Test
  fun `insert with the same adventureId`() {
    testScope.runBlockingTest {
      // Given we insert an Adventure
      // Given that we insert an adventure
      adventureDao.insert(adventure)

      // When we insert another Adventure copy with the same Id
      val newTitle = "Adventure title 2"
      adventureDao.insert(adventure.copy(title = newTitle))

      // Then we should not have the update
      assertThat(adventureDao.getAdventure(adventureId).title)
        .isNotEqualTo(newTitle)
    }
  }

  @Test
  fun `delete adventure by id`() {
    testScope.runBlockingTest {
      // Given we insert two Adventures
      adventureDao.insert(adventure)
      val secondAdventure = TestDatabaseUtils.createAdventure(UUID.randomUUID().toString()).copy(
        id = adventureUuid
      )
      adventureDao.insert(secondAdventure)

      // Then the database should contain 2 Adventures
      assertThat(adventureDao.getAdventures().size).isEqualTo(2)

      // When we delete one of the adventures
      adventureDao.deleteById(adventureUuid)

      // Then we should have only one adventure left
      assertThat(adventureDao.getAdventures().size).isEqualTo(1)
    }
  }

  @Test
  fun `delete all adventures`() {
    testScope.runBlockingTest {
      // Given we insert two Adventures
      adventureDao.insert(adventure)
      adventureDao.insert(
        adventure.copy(
          id = "258cb9c2-9752-4002-8ec0-d31dacc739d2"
        )
      )

      // Then the database should contain 2 Adventures
      assertThat(adventureDao.getAdventures().size).isEqualTo(2)

      // When we delete all the adventures
      adventureDao.deleteAll()

      // Then the database should be empty
      assertThat(adventureDao.getAdventures().size).isEqualTo(0)
    }
  }

  @After
  fun teardown() {
    database.close()
    testScope.cleanupTestCoroutines()
  }
}
