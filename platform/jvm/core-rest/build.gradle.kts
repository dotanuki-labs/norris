plugins {
    id("io.dotanuki.gradle.automodule")
    id("org.jetbrains.kotlin.plugin.serialization")
}

dependencies {
    implementation(projects.platform.jvm.coreNetworking)

    implementation(libs.square.okhttp.core)
    implementation(libs.square.okhttp.logging)
    implementation(libs.square.retrofit)
    implementation(libs.kotlinx.serialization.core)
    implementation(libs.kotlinx.serialization.json)
    implementation(libs.kotlinx.serialization.jvm)
    implementation(libs.kotlinx.coroutines.core)
    implementation(libs.jw.retrofit.kotlinx)

    implementation(libs.resilience4j.retry)
    implementation(libs.resilience4j.kotlin)

    testImplementation(libs.square.okhttp.mockwebserver)
    testImplementation(projects.platform.jvm.testingRest)
    testImplementation(projects.platform.jvm.testingHelpers)

    testImplementation(libs.google.truth)
    testImplementation(libs.junit4)
    testImplementation(libs.slf4j)
    testImplementation(libs.testcontainers.core)
    testImplementation(libs.testcontainers.toxyproxy)
    testImplementation(libs.testcontainers.mockserver)
    testImplementation(libs.mockserver.client)
}
