// Bridge to catch updates raised by Dependabot
// https://github.com/RBusarow/gradle-dependency-sync

plugins {
    id("com.rickbusarow.gradle-dependency-sync") version "0.11.4"
}

dependencies {

    // Gradle Plugins
    dependencySync("com.android.tools.build:gradle:7.0.3")
    dependencySync("com.adarshr:gradle-test-logger-plugin:3.0.0")
    dependencySync("org.jetbrains.kotlin:kotlin-gradle-plugin:1.5.31")
    dependencySync("org.jetbrains.kotlin:kotlin-serialization:1.5.31")
    dependencySync("org.jlleitschuh.gradle:ktlint-gradle:10.2.0")
    dependencySync("io.gitlab.arturbosch.detekt:detekt-gradle-plugin:1.18.1")
    dependencySync("com.karumi:shot:5.11.2")

    // Kodein
    dependencySync("org.kodein.di:kodein-di-jvm:7.9.0")
}
