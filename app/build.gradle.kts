plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.jetbrains.kotlin.android)

    // Annotation processing
    id(libs.plugins.ksp.get().pluginId)
    id(libs.plugins.kapt.get().pluginId)

    alias(libs.plugins.kotlin.serialization)

    id(libs.plugins.parcelize.get().pluginId)
    id(libs.plugins.hilt.get().pluginId)
    id(libs.plugins.safeArgs.get().pluginId)
}

android {
    namespace = "com.octagontechnologies.sky_weather"
    compileSdk = 34

    defaultConfig {
        val majorRelease = 1
        val defaultRelease = 3
        val minorRelease = 0

        applicationId = "com.octagontechnologies.sky_weather"
        minSdk = 24

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
                    "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

    composeOptions {
        kotlinCompilerExtensionVersion =("1.5.1")
    }

    buildFeatures {
        compose = true
        viewBinding = true
        //noinspection DataBindingWithoutKapt
        dataBinding = true
    }
    kotlinOptions {
        jvmTarget =("1.8")
        freeCompilerArgs = listOf("-Xallow-result-return-type")
    }
}

dependencies {
//
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
    implementation(libs.androidx.runtime)
    implementation(libs.androidx.runtime.livedata)


    // Compose UI libraries
    implementation(libs.compose.glide)
    implementation(libs.compose.googleFonts)

//     Network library
    implementation(libs.retrofit)
    implementation(libs.retrofit.moshiconverter)
    implementation(libs.retrofit.okhttp.logger)

    // Serialization
    implementation(libs.kotlin.serialization)
    implementation(libs.moshi)
    implementation(libs.moshi.reflect)
//    ksp(libs.ksp.moshi.codegen)

//    Local caching
    implementation(libs.room.runtime)
    implementation(libs.room.ktx)
    ksp(libs.room.compiler)

    // Others
    implementation(libs.logging.timber)
//    implementation(libs.permissionX)
    implementation(libs.jodaTime)


    testImplementation(libs.junit)
    testImplementation(libs.truth)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)

//    implementation fileTree(dir:("libs", include: ["*.jar"]))
//    implementation("org.jetbrains.kotlin:kotlin-stdlib:1.9.10")
//    implementation("androidx.core:core-ktx:1.12.0")
    implementation(libs.androidx.appcompat)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.androidx.navigation.fragment.ktx)
    implementation(libs.androidx.navigation.ui.ktx)
    implementation(libs.androidx.legacy.support.v4)
    implementation(libs.google.material)

    // Coordinator Layout
    implementation(libs.androidx.coordinatorlayout)

    // Google Android
    implementation(libs.play.services.location)

    // Dexter
    implementation(libs.dexter)

    // Test dependencies
    testImplementation(libs.junit)
    testImplementation(libs.hamcrest)
    testImplementation(libs.core.ktx)
    testImplementation(libs.robolectric)
    testImplementation(libs.androidx.junit.ktx)
    testImplementation(libs.androidx.core.testing)

    // Android Test dependencies
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)

    // Room and Lifecycle dependencies
//    implementation(libs.room.runtime)
//    implementation(libs.room.ktx)
//    ksp("androidx.room:room-compiler:2.6.1")

    // ViewModel
    implementation(libs.androidx.lifecycle.extensions)
    implementation(libs.androidx.lifecycle.viewmodel.ktx)

//    // Moshi
//    implementation("com.squareup.moshi:moshi:1.15.0")
//    implementation("com.squareup.moshi:moshi-kotlin:1.15.0")
//    implementation("org.jetbrains.kotlin:kotlin-reflect:1.9.0")

//    // Retrofit
//    implementation("com.squareup.retrofit2:retrofit:2.9.0")
//    implementation("com.squareup.retrofit2:converter-moshi:2.6.0")
//
//    // Coroutines
//    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.7.1")
//    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.7.1")

    // Glide
    implementation(libs.glide)

    // CircleImageView
    implementation(libs.circleimageview)

    // Groupie
    implementation(libs.groupie)
    implementation(libs.groupie.databinding)

    // Timber
    implementation(libs.logging.timber)

    // RecyclerView
    implementation(libs.recyclerview)

    // Preferences DataStore
    //noinspection GradleDependency
    implementation(libs.androidx.datastore.preferences)



    // Sdp
    implementation(libs.sdp.android)
    implementation(libs.ssp.android)

    // Okhttp Logging Interceptor
    implementation(libs.retrofit.okhttp.logger)

    // SwipeToRefresh
    implementation(libs.androidx.swiperefreshlayout)

    // Switch Button
    implementation(libs.library)

    // WorkManager
    // Kotlin + coroutines
    implementation(libs.androidx.work.runtime.ktx)

    // Hilt
    implementation(libs.hilt.android)
    implementation(libs.androidx.hilt.work)
    kapt(libs.hilt.compiler)
    kapt(libs.androidx.hilt.compiler)

    // Splash Screen
    implementation(libs.androidx.splashScreen)

    // Ads
    implementation(libs.play.services.ads)
}

kapt {
    correctErrorTypes = true
}