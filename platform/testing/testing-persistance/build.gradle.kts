
plugins {
    id("norris.plugins.shapers.platform.android")
}

dependencies {
    implementation(projects.platform.core.corePersistance)
    implementation(libs.kodein.di.jvm)
    implementation(libs.kodein.type.jvm)
    implementation(libs.kotlinx.coroutines.core)
    implementation(libs.kotlinx.coroutines.jvm)
    implementation(libs.androidx.test.monitor)
}
