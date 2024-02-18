import dev.iurysouza.modulegraph.Theme

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
        classpath(libs.gradle.emulator.wtf)
    }
}

plugins {
    id("io.dotanuki.gradle.featurematrix")
    id("dev.iurysouza.modulegraph") version "0.5.0"
}

allprojects {
    repositories {
        mavenCentral()
        google()
    }
}

moduleGraphConfig {
    readmePath.set("gradle-graph.md")
    heading.set("# Gradle Module Structure")
    theme.set(Theme.DARK)
}

tasks.run {
    register("clean").configure {
        delete("build")
    }
}
