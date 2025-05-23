plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    id("com.google.devtools.ksp")
    kotlin("plugin.serialization") version "2.1.10"
    id("com.google.android.libraries.mapsplatform.secrets-gradle-plugin")
    id("kotlin-parcelize")

}
android {
    namespace = "com.example.skycast"
    compileSdk = 35

    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.3"
    }
    defaultConfig {
        applicationId = "com.example.skycast"
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

    androidResources {
        generateLocaleConfig = true
    }
}



dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    implementation(libs.androidx.runtime.livedata)
    implementation(libs.play.services.location)
    implementation(libs.androidx.storage)
    implementation(libs.androidx.media3.ui)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)

    //room&retrofit
    implementation("com.squareup.retrofit2:converter-gson:2.11.0")
    implementation("com.google.code.gson:gson:2.12.1")
    implementation("com.github.bumptech.glide:compose:1.0.0-beta01")
    implementation("androidx.room:room-ktx:2.6.1")
    implementation("androidx.room:room-runtime:2.6.1")
    ksp("androidx.room:room-compiler:2.6.1")

    //navigation
    implementation("androidx.navigation:navigation-compose:2.8.9")
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.8.0")

    //location
    implementation("com.google.android.gms:play-services-location:21.3.0")


    // hamcrest
    testImplementation("org.hamcrest:hamcrest:2.2")
    testImplementation("org.hamcrest:hamcrest-library:2.2")
    androidTestImplementation("org.hamcrest:hamcrest:2.2")
    androidTestImplementation("org.hamcrest:hamcrest-library:2.2")


    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.7.3")

    // Coroutines Testing
    testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:1.7.3")

    // JUnit (for running tests)
    testImplementation("junit:junit:4.13.2")

    //maps
    val mapsComposeVersion = "4.4.1"
    implementation("com.google.maps.android:maps-compose:$mapsComposeVersion")
    implementation("com.google.maps.android:maps-compose-utils:$mapsComposeVersion")
    implementation("com.google.maps.android:maps-compose-widgets:$mapsComposeVersion")

    //viewmodel
    implementation("androidx.lifecycle:lifecycle-viewmodel-compose-android:2.8.7")

    //google play services
    implementation(libs.places)
    implementation(libs.play.services.maps)

    // shared pref settings
    implementation("androidx.preference:preference-ktx:1.2.0")


    //testing view model
    testImplementation("androidx.arch.core:core-testing:2.2.0")

/// work manager
    val work_version = "2.10.0"
    implementation("androidx.work:work-runtime-ktx:$work_version")

    //splash Screen
    implementation("androidx.core:core-splashscreen:1.0.0")

    // //localization
      implementation ("androidx.appcompat:appcompat:1.6.1")

    /// video backGround
    implementation ("androidx.media3:media3-exoplayer:1.3.0")

    //lottie
    implementation ("androidx.media3:media3-exoplayer:1.3.0")
    implementation ("androidx.media3:media3-ui:1.3.0")
    implementation("com.airbnb.android:lottie-compose:6.3.0")

}

secrets {
    // Optionally specify a different file name containing your secrets.
    // The plugin defaults to "local.properties"
    propertiesFileName = "secrets.properties"

    // A properties file containing default secret values. This file can be
    // checked in version control.
    defaultPropertiesFileName = "local.defaults.properties"
}
