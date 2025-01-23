// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.kotlin.android) apply false
}

buildscript {
    repositories {
        google()
        mavenCentral()
    }
    dependencies {
        classpath("com.android.tools.build:gradle:8.1.1") // Sesuaikan dengan versi terbaru dari Android Gradle Plugin
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:1.9.10") // Jika menggunakan Kotlin
        classpath("androidx.navigation:navigation-safe-args-gradle-plugin:2.7.2") // Untuk Safe Args Navigation
    }
}
