plugins {
    id("norris.modules.kotlin")
    id("org.jetbrains.kotlin.plugin.serialization")
}

dependencies {
    implementation(projects.platform.core.coreNetworking)

    implementation(libs.kodein.di.jvm)
    implementation(libs.kodein.type.jvm)

    implementation(libs.okhttp.core)
    implementation(libs.okhttp.logging)
    implementation(libs.retrofit)
    implementation(libs.kotlinx.serialization.core)
    implementation(libs.kotlinx.serialization.json)
    implementation(libs.kotlinx.serialization.jvm)
    implementation(libs.kotlinx.coroutines.core)

    testImplementation(projects.platform.testing.testingRest)

    testImplementation(libs.junit4)
    testImplementation(libs.truth)
    testImplementation(libs.slf4j)
    testImplementation(libs.kotlinx.coroutines.jvm)
}
