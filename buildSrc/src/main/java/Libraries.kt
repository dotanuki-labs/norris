import configs.KotlinConfig

// Versions for project parameters and forEachDependency

object Libraries {

    val kotlinStdlib = "org.jetbrains.kotlin:kotlin-stdlib-jdk8:${KotlinConfig.version}"
    val kotlinSerialization = "org.jetbrains.kotlinx:kotlinx-serialization-runtime:${Libraries.Versions.kotlinSerialization}"

    val okhttp = "com.squareup.okhttp3:okhttp:${Libraries.Versions.okHttp}"
    val okhttpLogger = "com.squareup.okhttp3:logging-interceptor:${Libraries.Versions.okHttp}"
    val retrofit = "com.squareup.retrofit2:retrofit:${Libraries.Versions.retrofit}"
    val retrofitKotlinSerialization = "com.jakewharton.retrofit:retrofit2-kotlinx-serialization-converter:${Libraries.Versions.retrofitKotlinSerialization}"

    val appCompat = "androidx.appcompat:appcompat:${Libraries.Versions.appCompat}"
    val recyclerView = "androidx.recyclerview:recyclerview:${Libraries.Versions.recyclerView}"

    val coreAndroidx = "androidx.core:core-ktx:1.2.0-alpha01"
    val lifecycleJava8 = "androidx.lifecycle:lifecycle-common-java8:${Libraries.Versions.lifecycle}"
    val lifecycleRuntime = "androidx.lifecycle:lifecycle-runtime-ktx:${Libraries.Versions.lifecycle}"
    val lifecycleViewModel = "androidx.lifecycle:lifecycle-viewmodel-ktx:${Libraries.Versions.lifecycle}"
    val lifecycleExtensions = "androidx.lifecycle:lifecycle-extensions:${Libraries.Versions.lifecycle}"

    val coroutinesCore = "org.jetbrains.kotlinx:kotlinx-coroutines-core:${Libraries.Versions.coroutines}"
    val coroutinesAndroid = "org.jetbrains.kotlinx:kotlinx-coroutines-android:${Libraries.Versions.coroutines}"

    val kodein = "org.kodein.di:kodein-di-generic-jvm:${Libraries.Versions.kodein}"
    val kodeinConf = "org.kodein.di:kodein-di-conf-jvm:${Libraries.Versions.kodein}"

    val jUnit = "junit:junit:${Libraries.Versions.junit}"
    val assertj = "org.assertj:assertj-core:${Libraries.Versions.assertj}"
    val burster = "com.github.ubiratansoares:burster:${Versions.burster}"
    val androidTestCore = "androidx.test:core:${Libraries.Versions.androidxTest}"
    val androidTestCoreKtx = "androidx.test:core-ktx:${Libraries.Versions.androidxTest}"
    val androidTestExtJunit = "androidx.test.ext:junit:${Libraries.Versions.androidxTest}"
    val androidTestExtJunitKtx = "androidx.test.ext:junit-ktx:${Libraries.Versions.androidxTest}"
    val androidTestRunner = "androidx.test:runner:${Libraries.Versions.androidxTest}"
    val androidTestRules = "androidx.test:rules:${Libraries.Versions.androidxTest}"
    val espressoCore = "androidx.test.espresso:espresso-core:${Libraries.Versions.espresso}"
    val mockWebServer = "com.squareup.okhttp3:mockwebserver:${Libraries.Versions.okHttp}"
    val slf4jNoOp = "org.slf4j:slf4j-nop:${Libraries.Versions.slf4j}"

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
        const val burster = "0.1.1"
        const val androidxTest = "1.2.0"
        const val espresso = "3.2.0"
        const val slf4j = "1.7.25"
    }
}