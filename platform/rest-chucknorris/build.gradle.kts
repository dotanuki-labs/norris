
plugins {
    id(BuildPlugins.Ids.kotlinModule)
    id(BuildPlugins.Ids.kotlinxSerialization)
}

dependencies {
    implementation(project(":platform:networking"))

    implementation("org.kodein.type:kodein-type-jvm:1.7.1")
    implementation("org.kodein.di:kodein-di-jvm:7.6.0")

    implementation("com.squareup.okhttp3:okhttp:4.9.1")
    implementation("com.squareup.okhttp3:logging-interceptor:4.9.1")
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-core:1.2.2")
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.2.2")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.5.1")
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-core-jvm:1.2.2")

    testImplementation("junit:junit:4.13.2")
    testImplementation("com.google.truth:truth:1.1.3")
    testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-core-jvm:1.5.1-native-mt")
}
