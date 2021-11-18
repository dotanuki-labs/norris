object Deps {

    private val deps by lazy {
        BuildSrcBuildScriptBridge.extractDependencies()
    }

    // Gradle Plugins
    val androidGradlePlugin by deps.coordinates("android-gradle-plugin")
    val kotlinGradlePlugin by deps.coordinates("kotlin-gradle-plugin")
    val testLoggerGradlePlugin by deps.coordinates("testlogger-gradle-plugin")
    val kotlinxSerializationGradlePlugin by deps.coordinates("kotlinx-serialization-gradle-plugin")
    val ktlintGradlePlugin by deps.coordinates("ktlint-gradle-plugin")
    val detektGradlePlugin by deps.coordinates("detekt-gradle-plugin")
    val shotGradlePlugin by deps.coordinates("testlogger-gradle-plugin")

    // Networking Libraries
    val retrofit by deps.coordinates("square-retrofit")

    private fun Map<String, String>.coordinates(alias: String) = lazy { getValue(alias) }
}
