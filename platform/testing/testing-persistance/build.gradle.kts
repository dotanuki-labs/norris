
plugins {
    id("norris.modules.android.platform")
}

dependencies {
    implementation(projects.platform.core.corePersistance)
    implementation(Deps.kodeinDi)
    implementation(Deps.kodeinType)
    implementation(Deps.coroutinesCore)
    implementation(Deps.coroutinesJvm)
}
