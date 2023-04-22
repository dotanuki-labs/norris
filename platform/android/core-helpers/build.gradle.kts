plugins {
    id("io.dotanuki.gradle.automodule")
}

dependencies {
    implementation(libs.androidx.appcompat)
    implementation(libs.androidx.lifecycle.common)
    implementation(libs.kotlinx.coroutines.jvm)
}
