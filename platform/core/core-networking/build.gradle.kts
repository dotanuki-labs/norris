
plugins {
    id("norris.modules.kotlin")
}

dependencies {

    implementation(libs.square.okhttp.core)
    implementation(libs.square.retrofit)
    implementation(libs.jw.retrofit.kotlinx)
    implementation(libs.kotlinx.serialization.core)
    implementation(libs.kotlinx.serialization.jvm)
    implementation(libs.kotlinx.serialization.json)
    implementation(libs.kotlinx.coroutines.core)

    testImplementation(libs.junit4)
    testImplementation(libs.google.truth)
    testImplementation(libs.kotlinx.coroutines.jvm)
}
