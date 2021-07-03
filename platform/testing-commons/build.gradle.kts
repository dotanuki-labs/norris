
plugins {
    id(BuildPlugins.Ids.kotlinModule)
}

dependencies {
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.5.0")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:1.5.0")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-debug:1.5.0")
    implementation("junit:junit:4.13.2")
    implementation("com.google.truth:truth:1.1.3")
}
