
plugins {
    id("io.dotanuki.gradle.automodule")
}

dependencies {
    implementation(libs.kotlinx.coroutines.core)
    implementation(libs.kotlinx.coroutines.jvm)
    implementation(libs.kotlinx.coroutines.test)
    implementation(libs.google.truth)
    implementation(libs.junit4)
}
