plugins {
  kotlin("multiplatform")
  kotlin("plugin.serialization")
  kotlin("native.cocoapods")
  id("com.android.library")
  id("org.jetbrains.compose")
  id("io.github.skeptick.libres")
  id("io.realm.kotlin")
  id("io.kotest.multiplatform")
}

kotlin {
  android()

  ios()
  iosSimulatorArm64()

  cocoapods {
    summary = "Some description for the Shared Module"
    homepage = "Link to the Shared Module homepage"
    version = "1.0"
    ios.deploymentTarget = "15.2"
    podfile = project.file("../ios/Podfile")
    framework {
      baseName = "shared"
      isStatic = true
    }
    extraSpecAttributes["resources"] = "['src/commonMain/libres/fonts/**']"
  }

  sourceSets {
    val commonMain by getting {

      dependencies {
        implementation(libs.koin.core)
        implementation(libs.logger.kermit)
        implementation(compose.ui)
        implementation(compose.foundation)
        implementation(compose.material)
        implementation(compose.runtime)
        @OptIn(org.jetbrains.compose.ExperimentalComposeLibrary::class)
        implementation(compose.components.resources)
        implementation(libs.libres.compose)
        implementation("org.jetbrains.kotlinx:atomicfu") {
          version {
            strictly("[0.20.2,)")
          }
          because("https://youtrack.jetbrains.com/issue/KT-57235")
        }
        implementation(libs.ktor.client.core)
        implementation(libs.ktor.client.negotiation)
        implementation(libs.ktor.serialization)
        implementation(libs.realm.base)
        implementation(libs.kotlinx.datetime)
        implementation(libs.kotlinx.immutable)
      }
    }
    val androidMain by getting {
      dependencies {
        implementation(libs.koin.android)
        implementation(libs.android.material)
        implementation(libs.androidx.fragment.ktx)
        implementation(libs.ktor.client.android)
        implementation(libs.androidx.compose.tooling)
        implementation(libs.androidx.compose.toolingPreview)
      }
    }
    val iosMain by getting {
      dependsOn(commonMain)
      dependencies {
        implementation(libs.logger.kermit)
        implementation(libs.ktor.client.darwin)
      }
    }
    val iosSimulatorArm64Main by getting {
      dependsOn(iosMain)
    }

    val commonTest by getting {
      dependencies {
        implementation(kotlin("test-common"))
        implementation(kotlin("test-annotations-common"))
        implementation(libs.kotest.assertions.core)
        implementation(libs.kotest.framework.engine)
        implementation(libs.kotest.framework.datatest)
        implementation(libs.kotest.property)
        implementation(libs.kotlinx.coroutines.core)
        implementation(libs.kotlinx.coroutines.test)
        implementation(libs.turbine)
      }
    }

    val androidUnitTest by getting {
      dependencies {
        implementation(libs.kotest.assertions.core)
        implementation(libs.kotest.framework.engine)
        implementation(libs.kotest.framework.datatest)
        implementation(libs.kotest.property)
        implementation(libs.kotest.runner.junit5)
        implementation(libs.turbine)
        implementation(libs.kotlinx.datetime)
        implementation(libs.mockk)
        implementation(libs.realm.base)
        implementation(libs.ktor.client.mock)

        implementation(kotlin("test-common"))
        implementation(kotlin("test-annotations-common"))
      }
    }

    val androidInstrumentedTest by getting {
      dependencies {
        implementation(kotlin("test"))
        implementation(kotlin("test-junit"))
        implementation(libs.instrumented.junit)
        implementation(libs.instrumented.junit.compose)
        implementation(libs.turbine)
        implementation(libs.kotest.assertions.core)
//        implementation(libs.kotest.framework.engine)
//        implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.6.4")
      }
    }
  }
}

libres {
  generatedClassName = "MainRes"
  generateNamedArguments = true
  baseLocaleLanguageCode = "en"
  camelCaseNamesForAppleFramework = true
}

android {
  namespace = "com.glowka.rafal.exchange"
  compileSdk = 33
  defaultConfig {
    minSdk = 24
    targetSdk = 33
  }
  compileOptions {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
  }

  packaging {
//    excludes += "/META-INF/LICENSE.md"
//    excludes += "/META-INF/LICENSE-notice.md"
    excludes += "/META-INF/{AL2.0,LGPL2.1}"
    excludes += "/META-INF/licenses/ASM"
    excludes += "/win32-x86-64/attach_hotspot_windows.dll"
    excludes += "/win32-x86/attach_hotspot_windows.dll"
  }

  defaultConfig {
    testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
  }

}

detekt {
  input = files(
    "src/androidInstrumentedTest/kotlin",
    "src/androidMain/kotlin",
    "src/androidUnitTest/kotlin",
    "src/commonMain/kotlin",
    "src/iosMain/kotlin",
    "src/testMain/kotlin",
  )
}