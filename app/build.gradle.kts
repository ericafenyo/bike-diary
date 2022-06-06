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
  id("com.android.application")
  id("kotlin-android")
  id("kotlin-kapt")
  id("kotlinx-serialization")
  id("dagger.hilt.android.plugin")
  id("androidx.navigation.safeargs")
}

android {
  compileSdk = libs.versions.compileSdk.get().toInt()

  defaultConfig {
    applicationId = "com.ericafenyo.bikediary"
    minSdk = libs.versions.minSdk.get().toInt()
    targetSdk = 31
    versionName = findProperty("VERSION_NAME").toString()
    versionCode = 1

    testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

    vectorDrawables.useSupportLibrary = true

    multiDexEnabled = true

    buildConfigField("String", "SENTRY_DSN", "\"${findProperty("SENTRY_DSN")}\"")
    buildConfigField("String", "MAPBOX_PUBLIC_TOKEN", "\"${findProperty("MAPBOX_PUBLIC_TOKEN")}\"")
  }

//  lintOptions {
//    checkReleaseBuilds(false)
//    checkDependencies = true
//  }

  buildTypes {
    release {
      isMinifyEnabled = false
      proguardFiles(
        getDefaultProguardFile("proguard-android-optimize.txt"),
        "proguard-rules.pro"
      )
    }
  }

  compileOptions {
    sourceCompatibility = JavaVersion.VERSION_1_8
    targetCompatibility = JavaVersion.VERSION_1_8
  }

  kotlinOptions {
    jvmTarget = "1.8"
//    allWarningsAsErrors = true
    freeCompilerArgs = freeCompilerArgs.toMutableList().apply {
      add("-Xopt-in=kotlin.RequiresOptIn")
    }.toList()
  }

  buildFeatures {
    dataBinding = true
    compose = true
    buildConfig = true
  }

  composeOptions {
    kotlinCompilerExtensionVersion = libs.versions.compose.get()
  }
}

dependencies {
  implementation(projects.domain)
  implementation(projects.logger)
  implementation(projects.model)
  implementation(projects.shared)
  implementation(projects.tracker)
  implementation(projects.libs.storage)
  implementation(projects.libs.icons)

  testImplementation(libs.junit)
  androidTestImplementation(libs.androidx.junitExt)
  androidTestImplementation(libs.androidx.espresso)

  implementation(libs.androidChart)

  implementation(libs.androidx.activity)
  implementation(libs.androidx.appcompat)
  implementation(libs.androidx.constraintlayout)
  implementation(libs.androidx.core)
  implementation(libs.androidx.fragment)
  implementation(libs.androidx.lifecycle.livedata)
  implementation(libs.androidx.lifecycle.runtime)
  implementation(libs.androidx.material)
  implementation(libs.androidx.navigation.fragment)
  implementation(libs.androidx.navigation.ui)
  implementation(libs.androidx.preference)
  implementation(libs.androidx.room.runtime)
  implementation(libs.androidx.securityCrypto)
  implementation(libs.androidx.work.hilt)
  implementation(libs.androidx.work.runtime)
  kapt(libs.androidx.work.hiltCompiler)

  implementation(libs.compose.activity)
  implementation(libs.compose.coil)
  implementation(libs.compose.constraintlayout)
  implementation(libs.compose.foundation)
  implementation(libs.compose.material)
  implementation(libs.compose.runtime)
  implementation(libs.compose.tooling)
  implementation(libs.compose.ui)
  implementation(libs.androidx.compose.navigation)

  implementation(libs.databinding)

  implementation(libs.flexbox)

  implementation(libs.glide)

  implementation(libs.google.gms.location)

  implementation(libs.hilt.android)
  kapt(libs.hilt.compiler)

  implementation(libs.kotlin.serialization.json)
  implementation(libs.kotlin.stdlib)

  implementation(libs.lottie)

  implementation(libs.mapbox.android)

  implementation(libs.materialratingbar)

  implementation(libs.sentry.android.runtime)

  implementation(libs.sentry.android.timber)

  implementation(libs.threetenabp)

  implementation(libs.timber)

//   debugImplementation(libs.leakCanary)
}

hilt {
  enableAggregatingTask = true
}
