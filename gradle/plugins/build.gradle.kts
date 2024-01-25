import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

repositories {
    google()
    mavenCentral()
    gradlePluginPortal()
}

plugins {
    java
    `kotlin-dsl`
    `java-gradle-plugin`
}

buildscript {
    repositories {
        google()
        mavenCentral()
    }
}

gradlePlugin {
    plugins {
        create("module-conventions-plugin") {
            id = "io.dotanuki.gradle.automodule"
            displayName = "AutoModule Gradle Plugin"
            implementationClass = "io.dotanuki.norris.gradle.modules.AutoModulePlugin"
        }

        create("feature-matrix-plugin") {
            id = "io.dotanuki.gradle.featurematrix"
            displayName = "Feature Matrix Gradle Plugin"
            implementationClass = "io.dotanuki.norris.gradle.features.FeatureMatrixPlugin"
        }
    }
}

tasks.withType<KotlinCompile>().configureEach {
    kotlinOptions.jvmTarget = "17"
    kotlinOptions.freeCompilerArgs +=
        listOf(
            "-opt-in=kotlin.time.ExperimentalTime",
            "-opt-in=kotlin.RequiresOptIn",
        )
}

tasks.withType<JavaCompile>().configureEach {
    targetCompatibility = JavaVersion.VERSION_17.toString()
    sourceCompatibility = JavaVersion.VERSION_17.toString()
}

dependencies {
    compileOnly(kotlin("stdlib-jdk8"))
    compileOnly(gradleApi())
    compileOnly(deps.gradle.android)
    compileOnly(deps.gradle.kotlin)
    compileOnly(deps.gradle.kotlin)
    compileOnly(deps.gradle.testlogger)
    compileOnly(deps.gradle.emulator.wtf)
}
