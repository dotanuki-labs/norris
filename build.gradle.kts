
buildscript {

    repositories {
        mavenCentral()
        google()
        maven(url = "https://plugins.gradle.org/m2/")
        maven(url = "https://maven.pkg.jetbrains.space/public/p/kotlinx-html/maven")
    }

    dependencies {
        classpath(libs.gradle.android)
        classpath(libs.gradle.kotlin)
        classpath(libs.gradle.kotlinx.serialization)
        classpath(libs.gradle.testlogger)
        classpath(libs.gradle.keeper)
        classpath(libs.gradle.dropshots)
        classpath(libs.gradle.oss.scan)
    }
}

plugins {
    id("io.dotanuki.gradle.security")
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
