plugins {
    id("norris.plugins.shapers.platform.kotlin")
}

dependencies {
    implementation(projects.platform.core.coreNetworking)
    implementation(projects.platform.core.coreRest)

    implementation(libs.okhttp.core)
    implementation(libs.okhttp.logging)
    implementation(libs.okhttp.mockwebserver)
    implementation(libs.retrofit)
    implementation(libs.kotlinx.serialization.json)
    implementation(libs.kodein.di.jvm)
    implementation(libs.kodein.type.jvm)
    implementation(libs.junit4)
}
