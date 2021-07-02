
plugins {
    id(BuildPlugins.Ids.kotlinModule)
}

dependencies {
    implementation(project(":platform:logger"))

    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8:1.5.20")
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-core:1.2.1")
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.2.1")
    implementation("com.squareup.okhttp3:okhttp:4.9.1")
    implementation("com.squareup.okhttp3:logging-interceptor:4.9.1")
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.jakewharton.retrofit:retrofit2-kotlinx-serialization-converter:0.8.0")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.5.0")
    implementation("org.kodein.di:kodein-di-jvm:7.6.0")

    testImplementation(project(":platform:testing-commons"))
    testImplementation("junit:junit:4.13.2")
    testImplementation("com.google.truth:truth:1.1.3")
    testImplementation("com.squareup.okhttp3:mockwebserver:4.9.1")
}
