
plugins {
    id(BuildPlugins.Ids.androidModule)
}

dependencies {
    implementation(project(":platform:logger"))
    implementation(project(":platform:persistance"))
    implementation("org.kodein.di:kodein-di-jvm:7.6.0")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.5.1")
    implementation("androidx.test:core:1.4.0")
    implementation("androidx.test:core-ktx:1.4.0")
}
