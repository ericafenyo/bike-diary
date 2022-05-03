/*
 * The MIT License (MIT)
 *
 * Copyright (C) 2021 TransWay
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

package com.ericafenyo.libs.storage

import kotlin.reflect.KClass
import kotlinx.coroutines.flow.Flow


interface FlowStorage {
  /**
   * Store a given Long value in the Storage.
   *
   * @param key  the key of the value to store.
   * @param value the value to store.
   */
  suspend fun store(key: String, value: Long)

  /**
   * Store a given Int value in the Storage.
   *
   * @param key  the key of the value to store.
   * @param value the value to store.
   */
  suspend fun store(key: String, value: Int)

  /**
   * Store a given value in the Storage.
   *
   * @param key  the key of the value to store.
   * @param value the value to store.
   */
  suspend fun store(key: String, value: String)

  /**
   * Store a given value in the Storage.
   *
   * @param key  the key of the value to store.
   * @param value the value to store.
   */
  suspend fun store(key: String, value: Boolean)

  /**
   * Store a given value in the Storage.
   *
   * @param key  the key of the value to store.
   * @param value the value to store.
   */
  suspend fun <T : Any> store(key: String, value: T)

  /**
   * Retrieve a value from the Storage.
   *
   * @param key the key of the value to retrieve.
   * @return the value that was previously saved.
   */
  fun retrieveLong(key: String): Flow<Long?>

  /**
   * Retrieve a value from the Storage.
   *
   * @param key the key of the value to retrieve.
   * @return the value that was previously saved.
   */

  fun retrieveString(key: String): Flow<String?>

  /**
   * Retrieve a value from the Storage.
   *
   * @param key the key of the value to retrieve.
   * @return the value that was previously saved.
   */
  fun retrieveInteger(key: String): Flow<Int?>

  /**
   * Retrieve a value from the Storage.
   *
   * @param key the key of the value to retrieve.
   * @return the value that was previously saved.
   */
  fun retrieveBoolean(key: String): Flow<Boolean?>

  /**
   * Retrieve a value from the Storage.
   *
   * @param key the key of the value to retrieve.
   * @return the value that was previously saved.
   */
  fun <T : Any> retrieve(key: String, clazz: KClass<T>): Flow<T?>

  /**
   * Removes a value from the storage.
   *
   * @param key the key of the value to remove.
   */
  suspend fun <T : Any> remove(key: String, clazz: KClass<T>)

  /**
   * Removes all value from the storage.
   */
  suspend fun clear()
}
