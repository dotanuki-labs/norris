
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
    }
}

dependencies {
    implementation("com.android.tools.build:gradle:7.0.3")
    implementation("com.adarshr:gradle-test-logger-plugin:3.1.0")
    implementation("org.jetbrains.kotlin:kotlin-gradle-plugin:1.5.31")
    implementation(kotlin("stdlib-jdk8"))
    implementation(kotlin("android-extensions"))
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
