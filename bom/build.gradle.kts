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
    api("${Lib.JUNIT}:${Version.JUNIT}")
    api("${Lib.KOTLIN_STDLIB}:${Version.KOTLIN}")

    api("${Lib.GLIDE}:${Version.GLIDE}")
    api("${Lib.GLIDE_COMPILER}:${Version.GLIDE}")

    api("${Lib.PREFERENCE_DATA_STORE}:${Version.PREFERENCE_DATA_STORE}")

    api("${Lib.ROOM_KTX}:${Version.ROOM}")
    api("${Lib.ROOM_RUNTIME}:${Version.ROOM}")
    api("${Lib.ROOM_COMPILER}:${Version.ROOM}")
    api("${Lib.ROOM_TESTING}:${Version.ROOM}")

    api("${Lib.GOOGLE_PLAY_SERVICES_LOCATION}:${Version.GOOGLE_PLAY_SERVICES_LOCATION}")

    api("${Lib.TIMBER}:${Version.TIMBER}")
  }
}

publishing {
  publications {
    create<MavenPublication>("myPlatform") {
      from(components["javaPlatform"])
    }
  }
}
