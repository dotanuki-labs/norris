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
            implementationClass = "io.dotanuki.norris.gradle.AutoModulePlugin"
        }
    }
}

tasks.withType<KotlinCompile>().configureEach {
    kotlinOptions.jvmTarget = "11"
    kotlinOptions.freeCompilerArgs += listOf(
        "-Xopt-in=kotlin.time.ExperimentalTime",
        "-Xopt-in=kotlin.RequiresOptIn"
    )
}

tasks.withType<JavaCompile>().configureEach {
    targetCompatibility = JavaVersion.VERSION_11.toString()
    sourceCompatibility = JavaVersion.VERSION_11.toString()
}

dependencies {
    compileOnly(kotlin("stdlib-jdk8"))
    compileOnly(gradleApi())
    compileOnly(deps.gradle.android)
    compileOnly(deps.gradle.kotlin)
    compileOnly(deps.gradle.testlogger)
    compileOnly(deps.gradle.keeper)
}
