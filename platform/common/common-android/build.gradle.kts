
plugins {
    id("norris.modules.android.platform")
}

dependencies {
    implementation(projects.platform.common.commonKodein)

    implementation("org.kodein.di:kodein-di-jvm:7.8.0")
    implementation("androidx.appcompat:appcompat:1.3.1")
    implementation("androidx.lifecycle:lifecycle-common:2.3.1")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core-jvm:1.5.2")
    implementation("org.kodein.type:kodein-type-jvm:1.11.0")
}
