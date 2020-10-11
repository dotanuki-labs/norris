
plugins {
    id(BuildPlugins.Ids.kotlinModule)
    id(BuildPlugins.Ids.kotlinxSerialization)
}

dependencies {
    implementation(project(":platform:logger"))
    implementation(project(":platform:domain"))
    implementation(project(":platform:networking"))

    implementation(Libraries.kotlinStdlib)
    implementation(Libraries.okhttp)
    implementation(Libraries.okhttpLogger)
    implementation(Libraries.retrofit)
    implementation(Libraries.kotlinSerializationCore)
    implementation(Libraries.kotlinSerializationJson)
    implementation(Libraries.coroutinesCore)
    implementation(Libraries.kodein)

    testImplementation(project(":platform:coroutines-testutils"))

    testImplementation(Libraries.jUnit)
    testImplementation(Libraries.assertj)
    testImplementation(Libraries.burster)
    testImplementation(Libraries.mockWebServer)
}