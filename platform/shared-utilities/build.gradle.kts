
plugins {
    id(BuildPlugins.Ids.androidModule)
}

dependencies {
    implementation("org.kodein.di:kodein-di-jvm:7.6.0")
    implementation("androidx.appcompat:appcompat:1.3.0")
    implementation("androidx.lifecycle:lifecycle-common:2.3.1")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core-jvm:1.5.1")
    implementation("org.kodein.type:kodein-type-jvm:1.7.1")
}
