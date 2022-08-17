
plugins {
    id("io.dotanuki.gradle.automodule")
}

dependencies {
    implementation(projects.platform.jvm.coreNetworking)
    implementation(projects.platform.jvm.coreRest)

    implementation(libs.square.okhttp.core)
    implementation(libs.square.okhttp.logging)
    implementation(libs.square.okhttp.mockwebserver)
    implementation(libs.square.retrofit)
    implementation(libs.kotlinx.serialization.json)
    implementation(libs.kodein.di.jvm)
    implementation(libs.junit4)
}
