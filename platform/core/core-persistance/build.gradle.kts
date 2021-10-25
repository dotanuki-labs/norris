
plugins {
    id("norris.modules.android.platform")
}

dependencies {

    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.5.2")
    implementation("org.kodein.di:kodein-di-jvm:7.9.0")
    implementation("org.kodein.type:kodein-type-jvm:1.11.0")

    testImplementation("junit:junit:4.13.2")
    testImplementation("com.google.truth:truth:1.1.3")
    testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-core-jvm:1.5.2")
}
