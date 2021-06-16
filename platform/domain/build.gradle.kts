
plugins {
    id(BuildPlugins.Ids.kotlinModule)
}

dependencies {
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8:1.5.10")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.5.0")
    testImplementation(project(":platform:testing-commons"))

    testImplementation("junit:junit:4.13.2")
    testImplementation("com.google.truth:truth:1.1.3")
    testImplementation("com.github.ubiratansoares:burster:0.1.1")
}
