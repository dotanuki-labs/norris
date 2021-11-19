
plugins {
    id("norris.modules.kotlin")
}

dependencies {
    implementation(Deps.coroutinesCore)
    implementation(Deps.coroutinesJvm)
    implementation(Deps.coroutinesTest)
    implementation(Deps.junit4)
    implementation(Deps.truth)
}
