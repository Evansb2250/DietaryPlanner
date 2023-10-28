buildscript {
    val agp_version4 by extra("8.0.0")
    dependencies {
        classpath("com.google.gms:google-services:4.3.14")
    }
}
// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    id("com.android.application") version "8.1.1" apply false
    id("org.jetbrains.kotlin.android") version "1.9.10" apply false
    id("com.google.dagger.hilt.android") version "2.48.1" apply false
    id("com.google.devtools.ksp") version "1.9.10-1.0.13" apply false
    id("de.mannodermaus.android-junit5") version "1.9.3.0"  apply false
}
