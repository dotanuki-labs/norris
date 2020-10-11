
plugins {
    id(BuildPlugins.Ids.kotlinModule)
}

dependencies {
    implementation(project(":platform:logger"))
    implementation(project(":platform:domain"))

    implementation(Libraries.kotlinStdlib)
    implementation(Libraries.kotlinSerializationCore)
    implementation(Libraries.kotlinSerializationJson)
    implementation(Libraries.okhttp)
    implementation(Libraries.okhttpLogger)
    implementation(Libraries.retrofit)
    implementation(Libraries.retrofitKotlinSerialization)
    implementation(Libraries.coroutinesCore)
    implementation(Libraries.kodein)

    testImplementation(project(":platform:coroutines-testutils"))

    testImplementation(Libraries.jUnit)
    testImplementation(Libraries.assertj)
    testImplementation(Libraries.burster)
    testImplementation(Libraries.mockWebServer)
}