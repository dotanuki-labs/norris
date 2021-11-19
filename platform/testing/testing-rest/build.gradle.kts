
plugins {
    id("norris.modules.kotlin")
}

dependencies {
    implementation(projects.platform.core.coreNetworking)
    implementation(projects.platform.core.coreRest)

    implementation(Deps.okHttp)
    implementation(Deps.okHttpLogging)
    implementation(Deps.okHttpMockWebServer)
    implementation(Deps.retrofit)
    implementation(Deps.kotlinSerializationJson)
    implementation(Deps.kodeinDi)
    implementation(Deps.kodeinType)
    implementation(Deps.junit4)
}
