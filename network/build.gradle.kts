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
  id("com.apollographql.apollo3")
}

android {
  compileSdk = libs.versions.compileSdk.get().toInt()

  defaultConfig {
    minSdk = libs.versions.minSdk.get().toInt()

    buildConfigField("String", "API_SERVER_URL", "\"${findProperty("API_SERVER_URL")}\"")
  }

  compileOptions {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
  }

  kotlin {
//    jvmToolchain(11)
  }

  buildFeatures {
    buildConfig = true
  }
  namespace = "com.ericafenyo.bikediary.network"
}

dependencies {
  implementation(projects.model)
  implementation(projects.libs.serialization)


  implementation((libs.okhttp3.loggingInterceptor))
  implementation((libs.okhttp3.okhttp))

  api(libs.apollo.runtime)

  implementation(libs.hilt.android)
  kapt(libs.hilt.compiler)

  implementation(libs.kotlin.serialization.json)

  implementation(libs.timber)
}

apollo {
  service("service") {
    packageName.set("com.ericafenyo.bikediary.graphql")

    // Mapping for GraphQL custom scalars
    mapScalarToKotlinString("DateTime")
    mapScalarToKotlinLong("Timestamp")
    mapScalarToKotlinLong("BigInt")
  }
}

kapt {
  correctErrorTypes = true
}
