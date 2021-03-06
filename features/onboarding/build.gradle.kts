
plugins {
    id(BuildPlugins.Ids.androidModule)
}

dependencies {
    implementation(project(":platform:logger"))
    implementation(project(":platform:shared-assets"))
    implementation(project(":platform:shared-utilities"))
    implementation(project(":platform:navigator"))

    implementation("org.kodein.di:kodein-di-jvm:7.6.0")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.5.1")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.5.1")
    implementation("androidx.lifecycle:lifecycle-extensions:2.2.0")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.3.1")
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.3.1")
    implementation("androidx.core:core-ktx:1.6.0")
    implementation("androidx.appcompat:appcompat:1.3.0")
    implementation("com.google.android.material:material:1.4.0")

    testImplementation(project(":platform:testing-commons"))

    testImplementation("junit:junit:4.13.2")
    testImplementation("com.google.truth:truth:1.1.3")
}
