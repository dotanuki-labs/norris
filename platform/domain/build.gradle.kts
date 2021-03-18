
plugins {
    id(BuildPlugins.Ids.kotlinModule)
}

dependencies {
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8:1.4.20")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.4.2-native-mt")

    testImplementation(project(":platform:coroutines-testutils"))
    testImplementation("junit:junit:4.13")
    testImplementation("org.assertj:assertj-core:3.16.1")
    testImplementation("com.github.ubiratansoares:burster:0.1.1")
}
