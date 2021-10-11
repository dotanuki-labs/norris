
plugins {
    id("norris.modules.kotlin")
}

dependencies {
    implementation(projects.platform.core.coreNetworking)
    implementation(projects.platform.core.coreRest)

    implementation("com.squareup.okhttp3:okhttp:4.9.2")
    implementation("com.squareup.okhttp3:logging-interceptor:4.9.2")
    implementation("com.squareup.okhttp3:mockwebserver:4.9.2")
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.3.0")
    implementation("org.kodein.di:kodein-di-jvm:7.8.0")
    implementation("org.kodein.type:kodein-type-jvm:1.9.0")
    implementation("junit:junit:4.13.2")
}
