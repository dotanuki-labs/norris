
plugins {
    id("norris.modules.android.platform")
}

dependencies {

    implementation(projects.platform.core.coreNavigator)
    implementation(projects.platform.core.coreRest)
    implementation(projects.platform.core.corePersistance)

    implementation(Deps.kodeinDi)
    implementation(Deps.kodeinType)
    implementation(Deps.androidxLifecycleCommon)
    implementation(Deps.okHttp)
    implementation(Deps.robolectric)
    implementation(Deps.androidxTestCore)
    implementation(Deps.androidxTestCoreKtx)
}
