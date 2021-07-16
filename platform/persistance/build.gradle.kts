
plugins {
    id(BuildPlugins.Ids.androidModule)
}

dependencies {

    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.5.1")
    implementation("org.kodein.di:kodein-di-jvm:7.6.0")
    implementation("org.kodein.type:kodein-type-jvm:1.7.1")

    testImplementation("junit:junit:4.13.2")
    testImplementation("com.google.truth:truth:1.1.3")
    testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-core-jvm:1.5.1")
}
