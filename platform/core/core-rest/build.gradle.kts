plugins {
    id("norris.modules.kotlin")
    id("org.jetbrains.kotlin.plugin.serialization")
}

dependencies {
    implementation(projects.platform.core.coreNetworking)

    implementation(libs.kodein.di.jvm)
    implementation(libs.square.okhttp.core)
    implementation(libs.square.okhttp.logging)
    implementation(libs.square.retrofit)
    implementation(libs.kotlinx.serialization.core)
    implementation(libs.kotlinx.serialization.json)
    implementation(libs.kotlinx.serialization.jvm)
    implementation(libs.kotlinx.coroutines.core)

    testImplementation(projects.platform.testing.testingRest)

    testImplementation(libs.google.truth)
    testImplementation(libs.junit4)
    testImplementation(libs.slf4j)
}
