
plugins {
    id("norris.modules.android.platform")
}

dependencies {
    implementation(projects.platform.core.corePersistance)
    implementation("org.kodein.di:kodein-di-jvm:7.8.0")
    implementation("org.kodein.type:kodein-type-jvm:1.11.0")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.5.2")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core-jvm:1.5.2")
    implementation("androidx.test:monitor:1.4.0")
}
