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

        register("platform-checks-plugin") {
            id = "norris.platform.checks"
            implementationClass = "plugins.PlatformChecksPlugin"
        }
    }
}
dependencies {
    project.logger.lifecycle("This project manages dependencies with Dependabot")

    implementation(kotlin("stdlib-jdk8"))
    implementation(kotlin("android-extensions"))
    implementation(deps.gradle.android)
    implementation(deps.gradle.kotlin)
    implementation(deps.gradle.testlogger)
    implementation(deps.gradle.keeper)
    implementation(deps.gradle.ktlint)
    implementation(deps.gradle.detekt)
    implementation(deps.gradle.oss.scan)
}
