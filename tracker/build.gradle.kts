/*
 * The MIT License (MIT)
 *
 * Copyright (C) 2022 Eric Afenyo
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

plugins {
  id("com.android.library")
  id("kotlin-android")
  id("kotlin-kapt")
  id("kotlinx-serialization")
  id("dagger.hilt.android.plugin")
}

android {
  namespace = "com.ericafenyo.tracker"

  compileSdk = libs.versions.compileSdk.get().toInt()

  defaultConfig {
    minSdk = libs.versions.minSdk.get().toInt()
  }

  compileOptions {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
  }

  kotlin {
//    jvmToolchain(11)
  }
}

dependencies {
  implementation(projects.network)
  implementation(projects.logger)
  implementation(projects.model)
  implementation(projects.shared)
  implementation(projects.libs.storage)
  implementation(projects.libs.serialization)

  implementation(libs.androidx.datastore)
  implementation(libs.androidx.room.ktx)
  implementation(libs.androidx.room.runtime)
  kapt(libs.androidx.room.compiler)

  implementation(libs.androidx.startup)

  implementation(libs.androidx.work.runtime)
  implementation(libs.androidx.work.hilt)
  implementation((libs.hilt.android))
  kapt(libs.hilt.compiler)
  kapt(libs.androidx.work.hiltCompiler)

  implementation(libs.google.gms.location)

  implementation(libs.kotlin.serialization.json)
  implementation(libs.kotlin.stdlib)

  implementation(libs.timber)
}
