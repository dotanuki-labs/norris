import configs.KotlinConfig

// Versions for project parameters and forEachDependency

object Libraries {

    const val kotlinStdlib = "org.jetbrains.kotlin:kotlin-stdlib-jdk8:1.4.20"
    const val kotlinSerializationCore = "org.jetbrains.kotlinx:kotlinx-serialization-core:1.0.1"
    const val kotlinSerializationJson = "org.jetbrains.kotlinx:kotlinx-serialization-json:1.0.1"

    const val okhttp = "com.squareup.okhttp3:okhttp:4.9.0"
    const val okhttpLogger = "com.squareup.okhttp3:logging-interceptor:4.9.0"
    const val retrofit = "com.squareup.retrofit2:retrofit:2.9.0"
    const val retrofitKotlinSerialization = "com.jakewharton.retrofit:retrofit2-kotlinx-serialization-converter:0.8.0"

    const val appCompat = "androidx.appcompat:appcompat:1.2.0"
    const val activity = "androidx.activity:activity:1.2.0-beta02"
    const val activityExtensions = "androidx.activity:activity-ktx:1.2.0-beta02"
    const val swipeToRefresh = "androidx.swiperefreshlayout:swiperefreshlayout:1.1.0"

    const val coreAndroidx = "androidx.core:core-ktx:1.3.1"
    const val materialDesign = "com.google.android.material:material:1.2.0"
    const val lifecycleRuntime = "androidx.lifecycle:lifecycle-runtime-ktx:2.2.0"
    const val lifecycleViewModel = "androidx.lifecycle:lifecycle-viewmodel-ktx:2.2.0"
    const val lifecycleExtensions = "androidx.lifecycle:lifecycle-extensions:2.2.0"

    const val coroutinesCore = "org.jetbrains.kotlinx:kotlinx-coroutines-core:1.4.1"
    const val coroutinesAndroid = "org.jetbrains.kotlinx:kotlinx-coroutines-android:1.4.1"
    const val coroutinesTest = "org.jetbrains.kotlinx:kotlinx-coroutines-test:1.4.1"
    const val coroutinesDebug = "org.jetbrains.kotlinx:kotlinx-coroutines-debug:1.4.1"

    const val kodein = "org.kodein.di:kodein-di-jvm:7.1.0"

    const val jUnit = "junit:junit:4.13"
    const val assertj = "org.assertj:assertj-core:3.16.1"
    const val assertjJava7 = "org.assertj:assertj-core:2.9.1"
    const val burster = "com.github.ubiratansoares:burster:0.1.1"
    const val turbine = "app.cash.turbine:turbine:0.2.1"
    const val barista = "com.schibsted.spain:barista:3.6.0"
    const val androidTestCore = "androidx.test:core:${Versions.androidxTest}"
    const val androidTestCoreKtx = "androidx.test:core-ktx:1.3.0"
    const val androidTestExtJunit = "androidx.test.ext:junit:1.1.2"
    const val androidTestExtJunitKtx = "androidx.test.ext:junit-ktx:1.1.2"
    const val androidTestRunner = "androidx.test:runner:1.3.0"
    const val androidTestRules = "androidx.test:rules:1.3.0"
    const val espressoCore = "androidx.test.espresso:espresso-core:3.3.0"
    const val roboletric = "org.robolectric:robolectric:4.4"
    const val mockWebServer = "com.squareup.okhttp3:mockwebserver:2.9.0"

    private object Versions {
        const val kotlinSerialization = "1.0.1"
        const val okHttp = "4.9.0"
        const val retrofit = "2.9.0"
        const val retrofitKotlinSerialization = "0.8.0"
        const val coroutines = "1.4.1"
        const val turbine = "0.2.1"
        const val kodein = "7.1.0"
        const val lifecycle = "2.2.0"
        const val coreAndroidx = "1.3.1"
        const val materialDesign = "1.2.0"
        const val appCompat = "1.2.0"
        const val activityX = "1.2.0-beta02"
        const val junit = "4.13"
        const val assertj = "3.16.1"
        const val burster = "0.1.1"
        const val jUnitKtx = "1.1.2"
        const val androidxTest = "1.3.0"
        const val espresso = "3.3.0"
        const val roboletric = "4.4"
        const val barista = "3.6.0"
    }
}
