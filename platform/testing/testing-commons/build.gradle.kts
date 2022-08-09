
plugins {
    id("norris.modules.kotlin")
}

dependencies {
    implementation(libs.kotlinx.coroutines.core)
    implementation(libs.kotlinx.coroutines.jvm)
    implementation(libs.kotlinx.coroutines.test)
    implementation(libs.google.truth)
    implementation(libs.junit4)
}
