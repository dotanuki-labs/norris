
buildscript {

    repositories {
        mavenCentral()
        google()
        gradlePluginPortal()
    }

    dependencies {
        classpath(libs.gradle.android)
        classpath(libs.gradle.kotlin)
        classpath(libs.gradle.kotlinx.serialization)
        classpath(libs.gradle.testlogger)
        classpath(libs.gradle.dropshots)
        classpath(libs.gradle.oss.scan)
        classpath(libs.gradle.emulator.wtf)
    }
}

plugins {
    id("io.dotanuki.gradle.security")
    id("io.dotanuki.gradle.featurematrix")
}

allprojects {
    repositories {
        mavenCentral()
        google()
    }
}

tasks.run {
    register("clean").configure {
        delete("build")
    }
}
