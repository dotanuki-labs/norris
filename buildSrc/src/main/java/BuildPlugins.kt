import configs.KotlinConfig

object BuildPlugins {

    object Dependencies {
        const val androidSupport = "com.android.tools.build:gradle:${Versions.agp}"
        const val testLogger = "com.adarshr:gradle-test-logger-plugin:${Versions.testLogger}"
        const val ktlint = "org.jlleitschuh.gradle:ktlint-gradle:${Versions.ktlint}"
        const val kotlinSupport = "org.jetbrains.kotlin:kotlin-gradle-plugin:${KotlinConfig.version}"
        const val kotlinxSerialization = "org.jetbrains.kotlin:kotlin-serialization:${KotlinConfig.version}"
    }

    object Ids {
        const val androidApplication = "com.android.application"
        const val androidLibrary = "com.android.library"
        const val kotlinJVM = "kotlin"
        const val kotlinAndroid = "kotlin-android"
        const val kotlinAndroidExtensions = "kotlin-android-extensions"
        const val testLogger = "com.adarshr.test-logger"
    }

    private object Versions {
        const val agp = "3.4.1"
        const val testLogger = "1.7.0"
        const val ktlint = "8.1.0"
    }
}