
plugins {
    id("norris.modules.android.feature")
}

dependencies {
    implementation(projects.platform.common.commonAndroid)
    implementation(projects.platform.common.commonKodein)
    implementation(projects.platform.common.commonStatic)
    implementation(projects.platform.core.coreRest)
    implementation(projects.platform.core.corePersistance)

    implementation(Deps.kodeinDi)
    implementation(Deps.kodeinType)
    implementation(Deps.coroutinesCore)
    implementation(Deps.coroutinesAndroid)
    implementation(Deps.coroutinesJvm)
    implementation(Deps.androidxCore)
    implementation(Deps.androidxCoreKtx)
    implementation(Deps.androidxLifecycleCommon)
    implementation(Deps.androidxLifecycleRuntime)
    implementation(Deps.androidxLifecycleViewModel)
    implementation(Deps.androidxLifecycleViewModelKtx)
    implementation(Deps.androidxAppCompat)
    implementation(Deps.androidxSwipeToRefresh)
    implementation(Deps.androidxCoordinatorLayout)
    implementation(Deps.androidxRecyclerView)
    implementation(Deps.googleMaterialDesign)

    testImplementation(projects.platform.testing.testingApp)
    testImplementation(projects.platform.testing.testingPersistance)
    testImplementation(projects.platform.testing.testingRest)

    testImplementation(Deps.junit4)
    testImplementation(Deps.truth)
    testImplementation(Deps.okHttpMockWebServer)
    testImplementation(Deps.androidxTestCore)
    testImplementation(Deps.androidxTestExtJunit)

    androidTestImplementation(projects.platform.core.coreNetworking)
    androidTestImplementation(projects.platform.testing.testingScreenshots)

    androidTestImplementation(Deps.junit4)
    androidTestImplementation(Deps.androidxTestCore)
    androidTestImplementation(Deps.androidxTestCoreKtx)
    androidTestImplementation(Deps.androidxTestExtJunit)
    androidTestImplementation(Deps.androidxTestExtJunitKtx)
    androidTestImplementation(Deps.androidxTestRules)
    androidTestImplementation(Deps.androidxTestRunner)
    androidTestImplementation(Deps.shotAndroid)
}
