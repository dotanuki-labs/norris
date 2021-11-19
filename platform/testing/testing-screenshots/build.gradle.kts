
plugins {
    id("norris.modules.android.platform")
}

dependencies {
    implementation(kotlin("stdlib-jdk8"))
    implementation(Deps.androidxTestCore)
    implementation(Deps.androidxTestCoreKtx)
    implementation(Deps.androidxLifecycleCommon)
}
