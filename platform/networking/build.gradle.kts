
plugins {
    id(BuildPlugins.Ids.kotlinModule)
}

dependencies {
    implementation(project(":platform:logger"))
    implementation(project(":platform:domain"))

    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8:1.4.20")
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-core:1.0.1")
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.0.1")
    implementation("com.squareup.okhttp3:okhttp:4.9.0")
    implementation("com.squareup.okhttp3:logging-interceptor:4.9.0")
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.jakewharton.retrofit:retrofit2-kotlinx-serialization-converter:0.8.0")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.4.1")
    implementation("org.kodein.di:kodein-di-jvm:7.3.1")

    testImplementation(project(":platform:coroutines-testutils"))

    testImplementation("junit:junit:4.13")
    testImplementation("org.assertj:assertj-core:3.16.1")
    testImplementation("com.github.ubiratansoares:burster:0.1.1")
    testImplementation("com.squareup.okhttp3:mockwebserver:4.9.0")
}
