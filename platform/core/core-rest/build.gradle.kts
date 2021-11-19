plugins {
    id("norris.modules.kotlin")
    id("org.jetbrains.kotlin.plugin.serialization")
}

dependencies {
    implementation(projects.platform.core.coreNetworking)

    implementation(Deps.kodeinDi)
    implementation(Deps.kodeinType)

    implementation(Deps.okHttp)
    implementation(Deps.okHttpLogging)
    implementation(Deps.retrofit)
    implementation(Deps.kotlinSerializationCore)
    implementation(Deps.kotlinSerializationJson)
    implementation(Deps.kotlinSerializationJvm)
    implementation(Deps.coroutinesCore)

    testImplementation(projects.platform.testing.testingRest)

    testImplementation(Deps.junit4)
    testImplementation(Deps.truth)
    testImplementation(Deps.slf4j)
    testImplementation(Deps.coroutinesJvm)
}
