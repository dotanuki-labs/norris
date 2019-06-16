object BuildPlugins {

    val androidSupport = "com.android.tools.build:gradle:${Versions.agp}"
    val testLogger = "com.adarshr:gradle-test-logger-plugin:${Versions.testLogger}"
    val kotlinSupport = "org.jetbrains.kotlin:kotlin-gradle-plugin:${KotlinConfig.version}"
    val kotlinxSerialization = "org.jetbrains.kotlin:kotlin-serialization:${KotlinConfig.version}"

    private object Versions {
        const val agp = "3.4.1"
        const val testLogger = "1.7.0"
    }
}