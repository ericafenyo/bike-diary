/*
* The MIT License (MIT)
*
* Copyright (C) 2020 Eric Afenyo
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
  id("java-platform")
  id("maven-publish")
}

@Suppress("UnstableApiUsage")
dependencies {
  constraints {
    // Dagger Hilt
    api("${Lib.ANDROIDX_HILT_COMPILER}:${Version.ANDROIDX_HILT_COMPILER}")
    api("${Lib.ANDROIDX_ANNOTATIONS}:${Version.ANDROIDX_ANNOTATIONS}")
    api("${Lib.HILT_ANDROID}:${Version.HILT}")
    api("${Lib.HILT_ANDROID}:${Version.HILT}")
    api("${Lib.HILT_COMPILER}:${Version.HILT}")
    api("${Lib.HILT_TESTING}:${Version.HILT}")
    api("${Lib.HILT_VIEWMODEL}:${Version.ANDROIDX_HILT_COMPILER}")
    api("${Lib.MAP_BOX}:${Version.MAP_BOX}")
    api("${Lib.MAP_BOX_ANNOTATIONS}:${Version.MAP_BOX_ANNOTATIONS}")

    api("${Lib.APPCOMPAT}:${Version.APPCOMPAT}")
    api("${Lib.BROWSER}:${Version.BROWSER}")
    api("${Lib.CONSTRAINT_LAYOUT}:${Version.CONSTRAINT_LAYOUT}")
    api("${Lib.DATABINDING_KTX}:${Version.DATABINDING_KTX}")
    api("${Lib.MATERIAL}:${Version.MATERIAL}")
    api("${Lib.PREFERENCE}:${Version.PREFERENCE}")
    api("${Lib.PREFERENCE_DATA_STORE}:${Version.PREFERENCE_DATA_STORE}")


    api("${Lib.LIFECYCLE_LIVE_DATA_KTX}:${Version.LIFECYCLE}")
    api("${Lib.LIFECYCLE_VIEW_MODEL_KTX}:${Version.LIFECYCLE}")
    api("${Lib.LIFECYCLE_COMPILER}:${Version.LIFECYCLE}")
    api("${Lib.NAVIGATION_FRAGMENT_KTX}:${Version.NAVIGATION}")
    api("${Lib.NAVIGATION_UI_KTX}:${Version.NAVIGATION}")
    api("${Lib.ROOM_KTX}:${Version.ROOM}")
    api("${Lib.ROOM_RUNTIME}:${Version.ROOM}")
    api("${Lib.ROOM_COMPILER}:${Version.ROOM}")

    api("${Lib.APPCOMPAT}:${Version.APPCOMPAT}")
    api("${Lib.ARCH_TESTING}:${Version.archTesting}")
    api("${Lib.CORE_KTX}:${Version.CORE_KTX}")
    api("${Lib.COROUTINES}:${Version.COROUTINES}")
    api("${Lib.COROUTINES_TEST}:${Version.COROUTINES}")
    api("${Lib.ESPRESSO_CORE}:${Version.ESPRESSO}")
    api("${Lib.ESPRESSO_CONTRIB}:${Version.ESPRESSO}")

    // Firebase
    api("${Lib.FIREBASE_DATABASE}:${Version.FIREBASE_DATABASE}")
    api("${Lib.FIREBASE_MESSAGING}:${Version.FIREBASE_MESSAGING}")
    api("${Lib.FIREBASE_CRASHLYTICS}:${Version.FIREBASE_CRASHLYTICS}")

    api("${Lib.FRAGMENT_KTX}:${Version.FRAGMENT}")
    api("${Lib.GLIDE}:${Version.GLIDE}")
    api("${Lib.GSON}:${Version.GSON}")
    api("${Lib.GLIDE_COMPILER}${Version.GSON}")

    api("${Lib.JUNIT}:${Version.JUNIT}")
    api("${Lib.EXT_JUNIT}:${Version.EXT_JUNIT}")
    api("${Lib.KOTLIN_STDLIB}:${Version.KOTLIN}")

    api("${Lib.NAVIGATION_FRAGMENT_KTX}:${Version.NAVIGATION}")
    api("${Lib.NAVIGATION_UI_KTX}:${Version.NAVIGATION}")

    api("${Lib.OKHTTP}:${Version.OKHTTP}")
    api("${Lib.OKHTTP_LOGGING_INTERCEPTOR}:${Version.OKHTTP}")
    api("${Lib.RUNNER}:${Version.runner}")
    api("${Lib.THREETENABP}:${Version.THREETENABP}")
    api("${Lib.TIMBER}:${Version.TIMBER}")
    api("${Lib.VIEWPAGER}:${Version.VIEWPAGER}")
    api("${Lib.RETROFIT}:${Version.RETROFIT}")
    api("${Lib.RETROFIT_GSON_CONVERTER}:${Version.RETROFIT}")
    api("${Lib.LOCAL_BROADCAST_MANAGER}:${Version.LOCAL_BROADCAST_MANAGER}")
    api("${Lib.GOOGLE_PLAY_SERVICES_LOCATION}:${Version.GOOGLE_PLAY_SERVICES_LOCATION}")

    api("${Lib.SECURITY_CRYPTO}:${Version.SECURITY_CRYPTO}")
  }
}

publishing {
  publications {
    create<MavenPublication>("myPlatform") {
      from(components["javaPlatform"])
    }
  }
}
