
plugins {
    id("norris.modules.android.platform")
}

dependencies {
    implementation(project(":platform:common:common-kodein"))
    implementation("org.kodein.di:kodein-di-jvm:7.7.0")
    implementation("androidx.appcompat:appcompat:1.3.1")
    implementation("androidx.lifecycle:lifecycle-common:2.3.1")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core-jvm:1.5.1")
    implementation("org.kodein.type:kodein-type-jvm:1.7.1")
}
