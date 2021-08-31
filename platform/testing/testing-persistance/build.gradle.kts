
plugins {
    id("norris.modules.android.platform")
}

dependencies {
    implementation(projects.platform.core.corePersistance)
    implementation("org.kodein.di:kodein-di-jvm:7.7.0")
    implementation("org.kodein.type:kodein-type-jvm:1.7.1")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.5.1")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core-jvm:1.5.1")
    implementation("androidx.test:monitor:1.4.0")
}
