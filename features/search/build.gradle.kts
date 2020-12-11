plugins {
    id(BuildPlugins.Ids.androidModule)
}

dependencies {
    implementation(project(":platform:domain"))
    implementation(project(":platform:logger"))
    implementation(project(":platform:navigator"))
    implementation(project(":platform:shared-assets"))
    implementation(project(":platform:shared-utilities"))

    implementation(Libraries.kotlinStdlib)
    implementation(Libraries.kodein)
    implementation(Libraries.coroutinesCore)
    implementation(Libraries.coroutinesAndroid)
    implementation(Libraries.lifecycleExtensions)
    implementation(Libraries.lifecycleRuntime)
    implementation(Libraries.lifecycleViewModel)
    implementation(Libraries.coreAndroidx)
    implementation(Libraries.appCompat)
    implementation(Libraries.materialDesign)
    implementation(Libraries.swipeToRefresh)

    testImplementation(project(":platform:coroutines-testutils"))
    testImplementation(Libraries.jUnit)
    testImplementation(Libraries.assertj)
    testImplementation(Libraries.turbine)
}
