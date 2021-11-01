
plugins {
    id("norris.modules.android.platform")
}

dependencies {
    implementation(projects.platform.core.corePersistance)
    implementation(libs.kodein.jvm)
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.5.2")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core-jvm:1.5.2")
    implementation("androidx.test:monitor:1.4.0")
}
