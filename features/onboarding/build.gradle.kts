
plugins {
    id(BuildPlugins.Ids.androidModule)
}

dependencies {
    implementation(project(":platform:logger"))
    implementation(project(":platform:shared-assets"))
    implementation(project(":platform:shared-utilities"))
    implementation(project(":platform:navigator"))

    implementation("org.kodein.di:kodein-di-jvm:7.6.0")
    implementation("org.kodein.type:kodein-type-jvm:1.7.1")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.5.1")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core-jvm:1.5.1-native-mt")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.5.1")
    implementation("androidx.appcompat:appcompat:1.3.0")
    implementation("androidx.lifecycle:lifecycle-common:2.3.1")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.3.1")
}
