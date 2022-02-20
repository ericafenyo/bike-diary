plugins {
  id("com.android.library")
  id("kotlin-android")
}

android {
  compileSdk = libs.versions.compileSdk.get().toInt()

  defaultConfig {
    minSdk = libs.versions.minSdk.get().toInt()
    testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
  }

  buildFeatures {
    compose = true
    buildConfig = false
  }

  compileOptions {
    sourceCompatibility = JavaVersion.VERSION_1_8
    targetCompatibility = JavaVersion.VERSION_1_8
  }

  composeOptions {
    kotlinCompilerExtensionVersion = libs.versions.compose.main.get()
  }

  lint {
//    disable.add("ObsoleteLintCustomCheck")
//    abortOnError = true
//    warningsAsErrors = true
  }
}

dependencies {
  implementation(libs.kotlin.stdlib)
  implementation(libs.androidx.core)
  implementation(libs.compose.runtime)
  implementation(libs.compose.ui)
}
