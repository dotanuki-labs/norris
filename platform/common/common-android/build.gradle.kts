plugins {
    id("norris.modules.android.platform")
}

dependencies {
    implementation(projects.platform.common.commonKodein)

    implementation(Deps.kodeinDi)
    implementation(Deps.kodeinType)
    implementation(Deps.androidxAppCompat)
    implementation(Deps.androidxLifecycleCommon)
    implementation(Deps.coroutinesJvm)
}
