plugins {
    id("io.dotanuki.gradle.automodule")
    id("org.jetbrains.kotlin.plugin.serialization")
}

dependencies {
    implementation(projects.platform.jvm.coreNetworking)

    implementation(libs.kodein.di.jvm)
    implementation(libs.square.okhttp.core)
    implementation(libs.square.okhttp.logging)
    implementation(libs.square.retrofit)
    implementation(libs.kotlinx.serialization.core)
    implementation(libs.kotlinx.serialization.json)
    implementation(libs.kotlinx.serialization.jvm)
    implementation(libs.kotlinx.coroutines.core)
    implementation("io.github.resilience4j:resilience4j-kotlin:1.3.1")
    implementation("io.github.resilience4j:resilience4j-retry:1.3.1")

    testImplementation(libs.square.okhttp.mockwebserver)
    testImplementation(projects.platform.jvm.testingRest)
    testImplementation(projects.platform.jvm.testingHelpers)

    testImplementation(libs.google.truth)
    testImplementation(libs.junit4)
    testImplementation(libs.slf4j)
}
