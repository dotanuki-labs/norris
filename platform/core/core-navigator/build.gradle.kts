
plugins {
    id("norris.modules.android.platform")
}

dependencies {
    implementation(projects.platform.common.commonKodein)

    implementation(Deps.androidxAppCompat)
    implementation(Deps.kodeinDi)

    testImplementation(projects.platform.testing.testingCommons)
    testImplementation(Deps.junit4)
    testImplementation(Deps.truth)
    testImplementation(Deps.robolectric)
}
