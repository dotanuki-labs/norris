plugins {
    id("norris.modules.kotlin")
    id("org.jetbrains.kotlin.plugin.serialization")
}

dependencies {
    implementation(projects.platform.core.coreNetworking)

    implementation("org.kodein.type:kodein-type-jvm:1.8.0")
    implementation("org.kodein.di:kodein-di-jvm:7.8.0")

    implementation("com.squareup.okhttp3:okhttp:4.9.1")
    implementation("com.squareup.okhttp3:logging-interceptor:4.9.1")
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-core:1.2.2")
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.3.0")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.5.1")
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-core-jvm:1.2.2")

    testImplementation(projects.platform.testing.testingRest)

    testImplementation("org.slf4j:slf4j-nop:1.7.32")
    testImplementation("junit:junit:4.13.2")
    testImplementation("com.google.truth:truth:1.1.3")
    testImplementation("au.com.dius.pact.consumer:junit:4.2.11")
    testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-core-jvm:1.5.1")
}
