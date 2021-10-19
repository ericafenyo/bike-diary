/*
 * The MIT License (MIT)
 *
 * Copyright (C) 2020 Transway
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

package com.ericafenyo.bikediary.network

import javax.inject.Inject
import javax.inject.Singleton
import okhttp3.Interceptor
import okhttp3.Response

@Singleton
class AccessTokenHttpInterceptor @Inject constructor(
//  private val user: AuthenticatedUser,
) : Interceptor {
  override fun intercept(chain: Interceptor.Chain): Response {
    val request = chain.request()
    val newRequest = request.newBuilder()
      .addHeader(
        "Authorization",
        "Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJlbWFpbCI6ImVyaWNAZXhhbXBsZS5jb20iLCJpYXQiOjE2MzM0MzQ3NTIsImV4cCI6MTYzMzUyMTE1MiwiYXVkIjoiaHR0cHM6Ly9iaWtlLWRpYXJ5Lmhlcm9rdWFwcC5jb20iLCJzdWIiOiJhdXRofDYxMzZiYjI3MTUxZTY4MjAzMDc3MTA0YSJ9.qsFJrXe7MbrwkS371dQss7yq7xjXTwsyD7-1-z3s0LQ"
      )
      .build()
    return chain.proceed(newRequest)
  }
}
