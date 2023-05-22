
plugins {
    id("io.dotanuki.gradle.automodule")
}

dependencies {
    implementation(projects.platform.android.corePersistence)

    implementation(libs.kotlinx.coroutines.core)
    implementation(libs.kotlinx.coroutines.jvm)
    implementation(libs.androidx.test.monitor)
}
