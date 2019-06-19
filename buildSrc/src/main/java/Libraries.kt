// Versions for project parameters and forEachDependency

object Libraries {

    val kotlinStdlib = "org.jetbrains.kotlin:kotlin-stdlib-jdk8:${KotlinConfig.version}"
    val kotlinSerialization = "org.jetbrains.kotlinx:kotlinx-serialization-runtime:${Versions.kotlinSerialization}"

    val okhttp = "com.squareup.okhttp3:okhttp:${Versions.okHttp}"
    val okhttpLogger = "com.squareup.okhttp3:logging-interceptor:${Versions.okHttp}"
    val retrofit = "com.squareup.retrofit2:retrofit:${Versions.retrofit}"
    val retrofitKotlinSerialization = "com.jakewharton.retrofit:retrofit2-kotlinx-serialization-converter:${Versions.retrofitKotlinSerialization}"

    val appCompat = "androidx.appcompat:appcompat:${Versions.appCompat}"
    val recyclerView = "androidx.recyclerview:recyclerview:${Versions.recyclerView}"

    val coreAndroidx = "androidx.core:core-ktx:1.2.0-alpha01"
    val lifecycleJava8 = "androidx.lifecycle:lifecycle-common-java8:${Versions.lifecycle}"
    val lifecycleRuntime = "androidx.lifecycle:lifecycle-runtime-ktx:${Versions.lifecycle}"
    val lifecycleViewModel = "androidx.lifecycle:lifecycle-viewmodel-ktx:${Versions.lifecycle}"
    val lifecycleExtensions = "androidx.lifecycle:lifecycle-extensions:${Versions.lifecycle}"

    val coroutinesCore = "org.jetbrains.kotlinx:kotlinx-coroutines-core:${Versions.coroutines}"
    val coroutinesAndroid = "org.jetbrains.kotlinx:kotlinx-coroutines-android:${Versions.coroutines}"

    val kodein = "org.kodein.di:kodein-di-generic-jvm:${Versions.kodein}"
    val kodeinConf = "org.kodein.di:kodein-di-conf-jvm:${Versions.kodein}"

    val jUnit = "junit:junit:${Versions.junit}"
    val assertj = "org.assertj:assertj-core:${Versions.assertj}"
    val androidTestCore = "androidx.test:core:${Versions.androidxTest}"
    val androidTestCoreKtx = "androidx.test:core-ktx:${Versions.androidxTest}"
    val androidTestExtJunit = "androidx.test.ext:junit:${Versions.androidxTest}"
    val androidTestExtJunitKtx = "androidx.test.ext:junit-ktx:${Versions.androidxTest}"
    val androidTestRunner = "androidx.test:runner:${Versions.androidxTest}"
    val androidTestRules = "androidx.test:rules:${Versions.androidxTest}"
    val espressoCore = "androidx.test.espresso:espresso-core:${Versions.espresso}"
    val mockWebServer = "com.squareup.okhttp3:mockwebserver:${Versions.okHttp}"
    val slf4jNoOp = "org.slf4j:slf4j-nop:${Versions.slf4j}"

    private object Versions {
        const val kotlinSerialization = "0.11.0"
        const val okHttp = "4.0.0-RC1"
        const val retrofit = "2.6.0"
        const val retrofitKotlinSerialization = "0.4.0"
        const val coroutines = "1.3.0-M1"
        const val kodein = "6.2.1"
        const val lifecycle = "2.2.0-alpha01"
        const val appCompat = "1.1.0-beta01"
        const val recyclerView = "1.1.0-alpha06"
        const val junit = "4.12"
        const val assertj = "3.11.1"
        const val androidxTest = "1.2.0"
        const val espresso = "3.2.0"
        const val slf4j = "1.7.25"
    }
}