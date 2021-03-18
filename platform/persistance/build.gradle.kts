
plugins {
    id(BuildPlugins.Ids.androidModule)
}

dependencies {

    implementation(project(":platform:domain"))
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.4.2-native-mt")
    implementation("org.kodein.di:kodein-di-jvm:7.3.1")

    testImplementation(project(":platform:coroutines-testutils"))
    testImplementation("junit:junit:4.13")
    testImplementation("org.assertj:assertj-core:3.16.1")
}
