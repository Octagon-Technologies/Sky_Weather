plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.jetbrains.kotlin.android)
    alias(libs.plugins.compose.compiler)

    // Annotation processing
    id(
        libs.plugins.ksp
            .get()
            .pluginId,
    )
    id(
        libs.plugins.kapt
            .get()
            .pluginId,
    )

    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.ktlint)

    id(
        libs.plugins.parcelize
            .get()
            .pluginId,
    )
    id(
        libs.plugins.hilt
            .get()
            .pluginId,
    )
    id(
        libs.plugins.safeArgs
            .get()
            .pluginId,
    )
}

android {
    namespace = "com.octagontechnologies.sky_weather"
    compileSdk = 36

    defaultConfig {
        val majorRelease = 2
        val defaultRelease = 0
        val minorRelease = 0

        applicationId = "com.octagontechnologies.sky_weather"
        minSdk = 24
        //noinspection OldTargetApi
        targetSdk = 34

        versionCode = (majorRelease * 100) + (defaultRelease * 10) + minorRelease
        versionName = "$majorRelease.$defaultRelease.$minorRelease"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro",
            )
        }
    }

    @Suppress("UnstableApiUsage")
    composeOptions {
        kotlinCompilerExtensionVersion = ("1.5.1")
    }

    buildFeatures {
        compose = true
        buildConfig = true
    }

    testOptions {
        unitTests.all { test ->
            test.useJUnitPlatform()
        }
    }
}

kotlin {
    jvmToolchain(17)
}

ktlint {
    additionalEditorconfig.put(
        "compose_allowed_composition_locals",
        "LocalAppColors",
    )
}

dependencies {

    // Core
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.splashScreen)
    implementation(libs.androidx.navigation)

    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.activity.compose)
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    implementation(libs.androidx.material.icons.extended)
    implementation("androidx.compose.foundation:foundation")

//    implementation(libs.androidx)

    implementation(libs.androidx.runtime)
    implementation(libs.androidx.runtime.livedata)
    implementation(libs.androidx.appcompat)
    implementation(libs.google.material)

    implementation(libs.androidx.lifecycle.extensions)
    implementation(libs.androidx.lifecycle.viewmodel.ktx)

    // Compose UI libraries
    implementation(libs.compose.glide)
    implementation(libs.compose.googleFonts)
    implementation(libs.compose.shimmer)
    implementation(libs.compose.cloudy)

    // Network library
    implementation(libs.retrofit)
    implementation(libs.retrofit.moshiconverter)
    implementation(libs.retrofit.okhttp.logger)

    // Serialization
    implementation(libs.kotlin.serialization)
    implementation(libs.moshi)
    implementation(libs.moshi.reflect)

    // Local caching
    implementation(libs.androidx.datastore.preferences)

    implementation(libs.room.runtime)
    implementation(libs.room.ktx)
    debugImplementation(libs.androidx.compose.ui.tooling)
    ksp(libs.room.compiler)

    // Others
    implementation(libs.logging.timber)
    implementation(libs.jodaTime)

    // Google Android
    implementation(libs.play.services.location)
    implementation(libs.androidx.work.runtime.ktx)

    // Timber
    implementation(libs.logging.timber)

    // Hilt
    implementation(libs.hilt.android)
    implementation(libs.androidx.hilt.work)
    implementation(libs.androidx.hilt.navigation.compose)
    kapt(libs.hilt.compiler)

    // Koin - Dependency Injection
    implementation(platform(libs.koin.bom))
    implementation(libs.bundles.koin)

    // Splash Screen
    implementation(libs.androidx.splashScreen)

    implementation(libs.core.immutable)
    ktlintRuleset(libs.ktlint.compose)

    // Unit Testing
    testImplementation(platform(libs.testing.junit.bom))
    testImplementation(libs.testing.unit.junit.jupiter)
    testRuntimeOnly(libs.testing.unit.junit.launcher)

    testImplementation(libs.testing.mock)
    testImplementation(libs.testing.coroutines)

    // Android Testing
//    androidTestImplementation(platform(libs.testing.junit.bom))
}

kapt {
    correctErrorTypes = true
}

tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile>().configureEach {
    compilerOptions {
        freeCompilerArgs.add("-Xallow-result-return-type")
    }
}
