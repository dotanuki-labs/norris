
plugins {
    id(BuildPlugins.Ids.androidModule)
}

dependencies {
    implementation("org.kodein.di:kodein-di-jvm:7.6.0")
    implementation("androidx.appcompat:appcompat:1.3.0")
    implementation("androidx.lifecycle:lifecycle-extensions:2.2.0")
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.3.1")
}
