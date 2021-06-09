
plugins {
    id(BuildPlugins.Ids.androidModule)
}

dependencies {

    implementation(project(":platform:domain"))
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.5.0")
    implementation("org.kodein.di:kodein-di-jvm:7.6.0")

    testImplementation(project(":platform:coroutines-testutils"))
    testImplementation("junit:junit:4.13.2")
    testImplementation("org.assertj:assertj-core:3.19.0")
}
