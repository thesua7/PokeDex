plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    id("androidx.room")


    id("com.google.devtools.ksp") version "2.0.0-Beta4-1.0.17"

    id("com.google.dagger.hilt.android")
}

android {
    namespace = "com.thesua7.pokedex"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.thesua7.pokedex"
        minSdk = 24
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
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
    sourceSets {
        getByName("main") {
            java.srcDir("build/generated/ksp/main/kotlin")
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }
    buildFeatures {
        compose = true
    }
    room {
        schemaDirectory("$projectDir/schemas")
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)


    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    implementation(libs.androidx.palette.ktx)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)


    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.9.0")

    val lifecycle_version = "2.8.7"


    // ViewModel
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:$lifecycle_version")
    // ViewModel utilities for Compose
    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:$lifecycle_version")
    // LiveData
    implementation("androidx.lifecycle:lifecycle-livedata-ktx:$lifecycle_version")
    // Lifecycles only (without ViewModel or LiveData)
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:$lifecycle_version")
    // Lifecycle utilities for Compose
    implementation("androidx.lifecycle:lifecycle-runtime-compose:$lifecycle_version")

    // Saved state module for ViewModel
    implementation("androidx.lifecycle:lifecycle-viewmodel-savedstate:$lifecycle_version")

    // Annotation processor
    ksp("androidx.lifecycle:lifecycle-compiler:$lifecycle_version")
    // alternately - if using Java8, use the following instead of lifecycle-compiler
    implementation("androidx.lifecycle:lifecycle-common-java8:$lifecycle_version")

    // retrofit
    implementation ("com.squareup.retrofit2:retrofit:2.11.0")
    implementation ("com.squareup.retrofit2:converter-gson:2.11.0")

    // ok https for interceptors
    implementation("com.squareup.okhttp3:okhttp:4.12.0")
    implementation ("com.squareup.okhttp3:logging-interceptor:4.12.0") // for loggin



    // timber
    implementation ("com.jakewharton.timber:timber:5.0.1")


    // compose
    implementation("androidx.compose.ui:ui:1.7.5")  // Jetpack Compose UI
    implementation("androidx.compose.material3:material3:1.3.1") // Material 3 for Compose
    implementation("androidx.compose.ui:ui-tooling-preview:1.7.5")  // For preview support
    implementation("androidx.activity:activity-compose:1.9.3") // For Compose integration with Activities
    implementation("androidx.navigation:navigation-compose:2.8.4") // For Compose navigation
    debugImplementation("androidx.compose.ui:ui-tooling:1.7.5")  // Debugging support for Compose
    debugImplementation("androidx.compose.ui:ui-tooling-preview:1.7.5")
    implementation("androidx.compose.foundation:foundation:1.7.5")  // Foundation for SwipeRefresh
    implementation("androidx.compose.foundation:foundation-layout:1.7.5")  // Foundation layout helpers



    // lottie animations for compose
    implementation("com.airbnb.android:lottie-compose:6.6.0")







    // room
    val room_version = "2.6.1"

    implementation("androidx.room:room-runtime:$room_version")
    ksp("androidx.room:room-compiler:$room_version")

    implementation("androidx.room:room-ktx:$room_version")


    // hilt
    implementation("com.google.dagger:hilt-android:2.51.1")
    ksp("com.google.dagger:hilt-android-compiler:2.51.1")
    implementation("androidx.hilt:hilt-navigation-fragment:1.2.0")
    implementation ("androidx.hilt:hilt-navigation-compose:1.2.0")   // If using Hilt compose


    // encryption
    implementation ("androidx.security:security-crypto:1.1.0-alpha06")

    // coil
    implementation("io.coil-kt.coil3:coil-compose:3.0.4")
    implementation("io.coil-kt.coil3:coil-gif:3.0.4")

    implementation ("com.squareup.picasso:picasso:2.8")
//    implementation("com.android.support:palette-v7:28.0.0")

//    // Coil
//    implementation ("io.coil-kt:coil:1.1.1")
//    implementation ("com.google.accompanist:accompanist-coil:0.7.0")


}