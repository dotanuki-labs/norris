import configs.KotlinConfig

object BuildPlugins {

    object Dependencies {
        const val androidSupport = "com.android.tools.build:gradle:${Versions.agp}"
        const val testLogger = "com.adarshr:gradle-test-logger-plugin:${Versions.testLogger}"
        const val ktlint = "org.jlleitschuh.gradle:ktlint-gradle:${Versions.ktlint}"
        const val kotlinSupport = "org.jetbrains.kotlin:kotlin-gradle-plugin:${KotlinConfig.version}"
        const val kotlinxSerialization = "org.jetbrains.kotlin:kotlin-serialization:${KotlinConfig.version}"
        const val jacocoUnified = "com.vanniktech:gradle-android-junit-jacoco-plugin:${Versions.jacocoUnified}"
        const val detekt = "io.gitlab.arturbosch.detekt:detekt-gradle-plugin:${Versions.detekt}"
    }

    object Ids {
        const val androidApplication = "com.android.application"
        const val androidLibrary = "com.android.library"
        const val kotlinJVM = "kotlin"
        const val kotlinxSerialization = "kotlinx-serialization"
        const val kotlinAndroid = "kotlin-android"
        const val kotlinAndroidExtensions = "kotlin-android-extensions"
        const val jacocoUnified = "com.vanniktech.android.junit.jacoco"
        const val sonarCloud = "org.sonarqube"
        const val detekt = "io.gitlab.arturbosch.detekt"
    }

    private object Versions {
        const val agp = "4.0.1"
        const val testLogger = "2.1.0"
        const val ktlint = "9.3.0"
        const val jacocoUnified = "0.16.0"
        const val detekt = "1.11.0-RC1"
    }
}