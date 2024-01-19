import java.util.Properties

plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("kotlin-android")
    id("com.google.devtools.ksp")
    id("de.mannodermaus.android-junit5")
    id("com.google.dagger.hilt.android")
}

android {
    namespace = "com.example.googlelightcalendar"
    compileSdk = 34

    testOptions{
        unitTests.all {
            it.ignoreFailures = false
            it.enabled = true
        }
    }

    defaultConfig {
        applicationId = "com.example.googlelightcalendar"
        minSdk = 26
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        manifestPlaceholders["appAuthRedirectScheme"] = "com.example.googlesigninappauth"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }

        val myPropert = Properties()
        myPropert.load(
            project.file("C:\\Users\\samue\\GoogleLightCalendar\\private_keys.properties")
                .inputStream()
        )

        val clientSecret = myPropert.getProperty("client")
        if (clientSecret != null)
            buildConfigField("String", "CLIENT_SECRET", "\"$clientSecret\"")

    }

    buildTypes {
        release {
            isMinifyEnabled = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = "17"
    }
    buildFeatures {
        buildConfig = true
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.3"
    }
    packaging {
        resources {
            excludes += "META-INF/*"
        }
    }

}

dependencies {
    implementation("com.google.firebase:firebase-inappmessaging-ktx:20.4.0")
    val lifecycle_version = "2.6.2"
    implementation("io.coil-kt:coil-compose:2.2.2")

    //Google Calendar imports

    implementation(fileTree(mapOf("dir" to "libs", "include" to listOf("*.jar"))))

    //Standard Implementations
    implementation("androidx.core:core-ktx:1.9.0")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.6.1")
    implementation("androidx.activity:activity-compose:1.7.2")
    implementation(platform("androidx.compose:compose-bom:2023.03.00"))
    implementation("androidx.compose.ui:ui")
    implementation("androidx.compose.ui:ui-graphics")
    implementation("androidx.compose.ui:ui-tooling-preview")
    implementation("androidx.compose.material3:material3")
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.9.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")

    /**
     * [Extra Implementations to build project]
     */


    implementation("com.google.dagger:hilt-android:2.48.1")
    ksp("com.google.dagger:hilt-android-compiler:2.48.1")
    implementation("androidx.hilt:hilt-navigation-compose:1.0.0")
    /*
     * ViewModel libraries
     */

    // ViewModel
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:$lifecycle_version")

    // ViewModel utilities for Compose
    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:$lifecycle_version")

    // Lifecycle utilities for Compose
    implementation("androidx.lifecycle:lifecycle-runtime-compose:$lifecycle_version")


    //AppAuth Libraries
    implementation("com.auth0.android:jwtdecode:2.0.0")
    implementation("net.openid:appauth:0.9.1")

    //OkHttp Library
    implementation("com.squareup.okhttp3:okhttp:4.11.0")

    //Scalar Factory
    implementation("com.squareup.retrofit2:converter-scalars:2.9.0")

    //MaterialDesign3
    implementation("androidx.compose.material3:material3:1.1.2")
    implementation("androidx.compose.material3:material3-window-size-class:1.1.2")

    //Splash Screen
    implementation("androidx.core:core-splashscreen:1.0.1")

    //Network
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")

    //Datastore
    implementation("androidx.datastore:datastore-preferences:1.0.0")

    //RoomDatabase
    val room_version = "2.6.0"
    implementation("androidx.room:room-runtime:$room_version")
    // optional - Kotlin Extensions and Coroutines support for Room
    implementation("androidx.room:room-ktx:$room_version")
    // optional - Test helpers
    testImplementation("androidx.room:room-testing:$room_version")
    // To use Kotlin annotation processing tool (kapt)
    ksp("androidx.room:room-compiler:$room_version")

    // Compose Navigation
    val nav_version = "2.7.4"
    implementation("androidx.navigation:navigation-compose:$nav_version")

    //TestImplementations
    // (Required) Writing and executing Unit Tests on the JUnit Platform
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.9.3")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.9.3")

    //Google Client Dependency for token Validation
    implementation("com.google.api-client:google-api-client:1.32.1")

    // (Optional) If you need "Parameterized Tests"
    testImplementation("org.junit.jupiter:junit-jupiter-params:5.9.3")

    // (Optional) If you also have JUnit 4-based tests
    testImplementation("junit:junit:4.13.2")
    testRuntimeOnly("org.junit.vintage:junit-vintage-engine:5.9.3")

    //Adding Assertion Library
    testImplementation("com.willowtreeapps.assertk:assertk:0.26.1")

    //AndroidTestImplementations
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
    androidTestImplementation(platform("androidx.compose:compose-bom:2023.03.00"))
    androidTestImplementation("androidx.compose.ui:ui-test-junit4")

    //For coroutines
    androidTestImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:1.6.4")
    debugImplementation("androidx.compose.ui:ui-tooling")
    debugImplementation("androidx.compose.ui:ui-test-manifest")

    // Mockito
    // Optional -- Mockito framework
    testImplementation("org.mockito:mockito-core:5.1.0")
    testImplementation("org.mockito.kotlin:mockito-kotlin:5.1.0")
    testImplementation("io.mockk:mockk:1.12.5")

    //Turbine dependency for testing flows
    testImplementation("app.cash.turbine:turbine:1.0.0")
    testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:1.7.1")
}