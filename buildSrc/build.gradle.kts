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

gradlePlugin {
    plugins {
        register("kotlin-module-plugin") {
            id = "norris.modules.kotlin"
            implementationClass = "plugins.NorrisKotlinModulePlugin"
        }

        register("android-platform-module-plugin") {
            id = "norris.modules.android.platform"
            implementationClass = "plugins.NorrisAndroidPlatformModulePlugin"
        }

        register("android-feature-module-plugin") {
            id = "norris.modules.android.feature"
            implementationClass = "plugins.NorrisAndroidFeatureModulePlugin"
        }

        register("android-application-module-plugin") {
            id = "norris.modules.android.app"
            implementationClass = "plugins.NorrisAndroidApplicationModulePlugin"
        }
    }
}

fun dependabot(target: String, alias: () -> String): String = target.apply {
    project.logger.lifecycle("[${alias()}] → $this ")
}

repositories {
    mavenCentral()
    google()
    gradlePluginPortal()
    maven(url = "https://maven.pkg.jetbrains.space/public/p/kotlinx-html/maven")
}

dependencies {

    project.logger.lifecycle("This project manages dependencies with Dependabot")

    implementation(kotlin("stdlib-jdk8"))
    implementation(kotlin("android-extensions"))

    // Used by plugins implemented at buildSrc but also required as dependencies
    // in other Gradle build scripts
    implementation(dependabot("com.android.tools.build:gradle:7.0.3") { "android-plugin" })
    implementation(dependabot("com.adarshr:gradle-test-logger-plugin:3.1.0") { "testlogger-gradle-plugin" })
    implementation(dependabot("org.jetbrains.kotlin:kotlin-gradle-plugin:1.6.0") { "kotlin-gradle-plugin" })

    // Used outside buildSrc, either as build script dependency or some configuration
    dependabot("org.jetbrains.kotlin:kotlin-serialization:1.6.0") { "kotlinx-serialization-gradle-plugin" }
    dependabot("org.jlleitschuh.gradle:ktlint-gradle:10.2.0") { "ktlint-gradle-plugin" }
    dependabot("io.gitlab.arturbosch.detekt:detekt-gradle-plugin:1.18.1") { "detekt-gradle-plugin" }
    dependabot("com.karumi:shot:5.11.2") { "shot-gradle-plugin" }

    dependabot("com.squareup.retrofit2:retrofit:2.9.0") { "square-retrofit" }
}
