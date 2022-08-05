plugins {
    id("norris.modules.android.feature")
}

dependencies {

    implementation(projects.platform.common.commonAndroid)
    implementation(projects.platform.common.commonKodein)
    implementation(projects.platform.common.commonStatic)
    implementation(projects.platform.core.coreNavigator)
    implementation(projects.platform.core.coreNetworking)
    implementation(projects.platform.core.coreRest)
    implementation(projects.platform.core.corePersistance)

    implementation(Deps.kodeinDi)
    implementation(Deps.coroutinesCore)
    implementation(Deps.coroutinesAndroid)
    implementation(Deps.coroutinesJvm)
    implementation(Deps.androidxLifecycleCommon)
    implementation(Deps.androidxLifecycleRuntime)
    implementation(Deps.androidxLifecycleViewModel)
    implementation(Deps.androidxLifecycleViewModelKtx)
    implementation(Deps.androidxAppCompat)
    implementation(Deps.androidxSwipeToRefresh)
    implementation(Deps.androidxCoordinatorLayout)
    implementation(Deps.androidxRecyclerView)
    implementation(Deps.androidxCore)
    implementation(Deps.googleMaterialDesign)

    testImplementation(projects.platform.testing.testingApp)
    testImplementation(projects.platform.testing.testingCommons)
    testImplementation(projects.platform.testing.testingRest)
    testImplementation(projects.platform.testing.testingPersistance)

    testImplementation(Deps.junit4)
    testImplementation(Deps.truth)
    testImplementation(Deps.okHttpMockWebServer)
    testImplementation(Deps.androidxTestCore)
    testImplementation(Deps.androidxTestExtJunit)
    testImplementation(Deps.robolectric)

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
