
plugins {
    id("norris.modules.kotlin")
}

dependencies {

    implementation(Deps.okHttp)
    implementation(Deps.retrofit)
    implementation(Deps.retrofitKotlixConverter)
    implementation(Deps.kotlinSerializationCore)
    implementation(Deps.kotlinSerializationJvm)
    implementation(Deps.kotlinSerializationJson)
    implementation(Deps.coroutinesCore)

    testImplementation(Deps.junit4)
    testImplementation(Deps.truth)
    testImplementation(Deps.coroutinesJvm)
}
