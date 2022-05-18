
plugins {
    id("norris.plugins.shapers.platform.kotlin")
}

dependencies {

    implementation(libs.okhttp.core)
    implementation(libs.retrofit)
    implementation(libs.jw.retrofit.kotlinx.serialisation)
    implementation(libs.kotlinx.serialization.core)
    implementation(libs.kotlinx.serialization.jvm)
    implementation(libs.kotlinx.serialization.json)
    implementation(libs.kotlinx.coroutines.core)

    testImplementation(libs.junit4)
    testImplementation(libs.truth)
    testImplementation(libs.kotlinx.coroutines.jvm)
}
