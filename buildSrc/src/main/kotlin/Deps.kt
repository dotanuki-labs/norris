object Deps {

    private val deps by lazy {
        BuildSrcBuildScriptBridge.extractDependencies()
    }

    // Gradle Plugins
    val androidGradlePlugin by deps.coordinates("android-gradle-plugin")
    val kotlinGradlePlugin by deps.coordinates("kotlin-gradle-plugin")
    val testLoggerGradlePlugin by deps.coordinates("testlogger-gradle-plugin")
    val kotlinxSerializationGradlePlugin by deps.coordinates("kotlinx-serialization-gradle-plugin")
    val shotGradlePlugin by deps.coordinates("shot-gradle-plugin")
    val keeperGradlePlugin by deps.coordinates("keeper-gradle-plugin")

    // Kodein
    val kodeinDi by deps.coordinates("kodein-di-jvm")
    val kodeinType by deps.coordinates("kodein-type-jvm")

    // Kotlinx
    val coroutinesCore by deps.coordinates("kotlinx-coroutines-core")
    val coroutinesJvm by deps.coordinates("kotlinx-coroutines-jvm")
    val coroutinesAndroid by deps.coordinates("kotlinx-coroutines-android")
    val kotlinSerializationCore by deps.coordinates("kotlinx-serialization-core")
    val kotlinSerializationJvm by deps.coordinates("kotlinx-serialization-jvm")
    val kotlinSerializationJson by deps.coordinates("kotlinx-serialization-json")

    // Androix
    val androidxCore by deps.coordinates("androidx-core")
    val androidxCoreKtx by deps.coordinates("androidx-core-ktx")
    val androidxLifecycleCommon by deps.coordinates("androidx-lifecycle-common")
    val androidxLifecycleRuntime by deps.coordinates("androidx-lifecycle-runtime")
    val androidxLifecycleViewModel by deps.coordinates("androidx-lifecycle-vm")
    val androidxLifecycleViewModelKtx by deps.coordinates("androidx-lifecycle-vm-ktx")
    val androidxAppCompat by deps.coordinates("androidx-appcompat")
    val androidxSwipeToRefresh by deps.coordinates("androidx-swipetorefresh")
    val androidxCoordinatorLayout by deps.coordinates("androidx-coordinatorlayout")
    val androidxRecyclerView by deps.coordinates("androidx-recyclerview")
    val googleMaterialDesign by deps.coordinates("google-material-design")

    // Networking
    val okHttp by deps.coordinates("square-okhttp")
    val okHttpLogging by deps.coordinates("square-okhttp-logging")
    val retrofit by deps.coordinates("square-retrofit")
    val retrofitKotlixConverter by deps.coordinates("retrofit-kotlinx-converter")

    // Testing
    val junit4 by deps.coordinates("junit4")
    val slf4j by deps.coordinates("slf4j")
    val robolectric by deps.coordinates("robolectric")
    val truth by deps.coordinates("truth")
    val coroutinesTest by deps.coordinates("kotlinx-coroutines-test")
    val androidxTestCore by deps.coordinates("androidx-test-core")
    val androidxTestCoreKtx by deps.coordinates("androidx-test-corektx")
    val androidxTestExtJunit by deps.coordinates("androidx-testext-junit")
    val androidxTestExtJunitKtx by deps.coordinates("androidx-testext-junitktx")
    val androidxTestRunner by deps.coordinates("androidx-test-runner")
    val androidxTestRules by deps.coordinates("androidx-test-rules")
    val androidxTestMonitor by deps.coordinates("androidx-test-monitor")
    val okHttpMockWebServer by deps.coordinates("okhttp-mockwebserver")
    val shotAndroid by deps.coordinates("shot-android")
    val leakCanary by deps.coordinates("leak-canary-release")
    val espressoCore by deps.coordinates("espresso-core")
    val barista by deps.coordinates("barista")

    private fun Map<String, String>.coordinates(alias: String) = lazy { getValue(alias) }
}
