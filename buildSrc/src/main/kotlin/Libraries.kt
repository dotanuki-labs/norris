import configs.KotlinConfig

// Versions for project parameters and forEachDependency

object Libraries {

    const val kotlinStdlib = "org.jetbrains.kotlin:kotlin-stdlib-jdk8:${KotlinConfig.version}"
    const val kotlinSerializationCore = "org.jetbrains.kotlinx:kotlinx-serialization-core:${Versions.kotlinSerialization}"
    const val kotlinSerializationJson = "org.jetbrains.kotlinx:kotlinx-serialization-json:${Versions.kotlinSerialization}"

    const val okhttp = "com.squareup.okhttp3:okhttp:${Versions.okHttp}"
    const val okhttpLogger = "com.squareup.okhttp3:logging-interceptor:${Versions.okHttp}"
    const val retrofit = "com.squareup.retrofit2:retrofit:${Versions.retrofit}"
    const val retrofitKotlinSerialization = "com.jakewharton.retrofit:retrofit2-kotlinx-serialization-converter:${Versions.retrofitKotlinSerialization}"

    const val appCompat = "androidx.appcompat:appcompat:${Versions.appCompat}"
    const val activity = "androidx.activity:activity:${Versions.activityX}"
    const val activityExtensions = "androidx.activity:activity-ktx:${Versions.activityX}"
    const val swipeToRefresh = "androidx.swiperefreshlayout:swiperefreshlayout:1.1.0"

    const val coreAndroidx = "androidx.core:core-ktx:${Versions.coreAndroidx}"
    const val materialDesign = "com.google.android.material:material:${Versions.materialDesign}"
    const val lifecycleRuntime = "androidx.lifecycle:lifecycle-runtime-ktx:${Versions.lifecycle}"
    const val lifecycleViewModel = "androidx.lifecycle:lifecycle-viewmodel-ktx:${Versions.lifecycle}"
    const val lifecycleExtensions = "androidx.lifecycle:lifecycle-extensions:${Versions.lifecycle}"

    const val coroutinesCore = "org.jetbrains.kotlinx:kotlinx-coroutines-core:${Versions.coroutines}"
    const val coroutinesAndroid = "org.jetbrains.kotlinx:kotlinx-coroutines-android:${Versions.coroutines}"
    const val coroutinesTest = "org.jetbrains.kotlinx:kotlinx-coroutines-test:${Versions.coroutines}"
    const val coroutinesDebug = "org.jetbrains.kotlinx:kotlinx-coroutines-debug:${Versions.coroutines}"

    const val kodein = "org.kodein.di:kodein-di-jvm:${Versions.kodein}"

    const val jUnit = "junit:junit:${Versions.junit}"
    const val assertj = "org.assertj:assertj-core:${Versions.assertj}"
    const val assertjJava7 = "org.assertj:assertj-core:2.9.1"
    const val burster = "com.github.ubiratansoares:burster:${Versions.burster}"
    const val turbine = "app.cash.turbine:turbine:${Versions.turbine}"
    const val barista = "com.schibsted.spain:barista:${Versions.barista}"
    const val androidTestCore = "androidx.test:core:${Versions.androidxTest}"
    const val androidTestCoreKtx = "androidx.test:core-ktx:${Versions.androidxTest}"
    const val androidTestExtJunit = "androidx.test.ext:junit:${Versions.jUnitKtx}"
    const val androidTestExtJunitKtx = "androidx.test.ext:junit-ktx:${Versions.jUnitKtx}"
    const val androidTestRunner = "androidx.test:runner:${Versions.androidxTest}"
    const val androidTestRules = "androidx.test:rules:${Versions.androidxTest}"
    const val espressoCore = "androidx.test.espresso:espresso-core:${Versions.espresso}"
    const val roboletric = "org.robolectric:robolectric:${Versions.roboletric}"
    const val mockWebServer = "com.squareup.okhttp3:mockwebserver:${Versions.okHttp}"

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
