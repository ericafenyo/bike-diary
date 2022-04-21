plugins {
  id("com.android.application")
  id("kotlin-android")
  id("kotlin-kapt")
  id("kotlinx-serialization")
  id("dagger.hilt.android.plugin")
  id("androidx.navigation.safeargs")
}

val SENTRY_DSN: Any by project
val MAPBOX_ACCESS_TOKEN: Any by project

android {
  compileSdk = libs.versions.compileSdk.get().toInt()

  defaultConfig {
    applicationId = "com.ericafenyo.bikediary"
    minSdk = libs.versions.minSdk.get().toInt()
    targetSdk = 31
    versionName = project.findProperty("VERSION_NAME").toString()
    versionCode = 1

    testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

    vectorDrawables.useSupportLibrary = true

    multiDexEnabled = true

    buildConfigField("String", "SENTRY_DSN", "\"$SENTRY_DSN\"")
    buildConfigField("String", "MAPBOX_ACCESS_TOKEN", "\"$MAPBOX_ACCESS_TOKEN\"")
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
  implementation(project(":domain"))
  implementation(project(":logger"))
  implementation(project(":model"))
  implementation(project(":shared"))
  implementation(project(":tracker"))
  implementation(project(":libs:storage"))
  implementation(project(":libs:icons"))

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
  implementation(libs.mapbox.annotation)

  implementation(libs.materialratingbar)

  implementation(libs.sentry.android.runtime)

  implementation(libs.sentry.android.timber)

  implementation(libs.threetenabp)

  implementation(libs.timber)

//   debugImplementation(libs.leakCanary)
}

hilt {
  enableExperimentalClasspathAggregation = true
}
