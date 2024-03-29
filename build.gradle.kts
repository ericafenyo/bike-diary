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

buildscript {
  repositories {
    google()
    mavenCentral()
  }

  dependencies {
    classpath(libs.android.gradlePlugin)
    classpath(libs.kotlin.gradlePlugin)
    classpath(libs.kotlin.serialization.gradlePlugin)
    classpath(libs.hilt.gradlePlugin)
    classpath(libs.apollo.gradlePlugin)
    classpath(libs.dokka)
    classpath(libs.androidx.navigation.gradlePlugin)
    // NOTE: Do not place your application dependencies here; they belong
    // in the individual module build.gradle files
  }
}

plugins {
//  id("com.osacky.doctor") version "0.8.1"
//  id "com.github.ben-manes.versions" version "0.44.0"
}

allprojects {
  repositories {
    google()
    mavenCentral()
    maven {
      url = uri("https://api.mapbox.com/downloads/v2/releases/maven")
      authentication {
        create<BasicAuthentication>("basic")
      }
      credentials {
        username = "mapbox"
        password = property("MAPBOX_DOWNLOAD_TOKEN") as String?
      }
    }
    maven { url = uri("https://jitpack.io") }
  }
}

task("clean") {
  delete("${rootProject.buildDir}")
}
