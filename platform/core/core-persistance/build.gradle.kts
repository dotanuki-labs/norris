
plugins {
    id("norris.modules.android.platform")
}

dependencies {

    implementation(Deps.coroutinesCore)
    implementation(Deps.kodeinDi)

    testImplementation(Deps.junit4)
    testImplementation(Deps.truth)
    testImplementation(Deps.coroutinesJvm)
}
