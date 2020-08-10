import dependencies.UnitTestDependencies.Companion.unitTest

plugins {
    id(BuildPlugins.Ids.androidModule)
}

dependencies {
    implementation(Libraries.kotlinStdlib)
    implementation(Libraries.kodein)
    implementation(Libraries.coroutinesCore)
    implementation(Libraries.coroutinesAndroid)
    implementation(Libraries.lifecycleRuntime)
    implementation(Libraries.coreAndroidx)
    implementation(Libraries.appCompat)
    implementation(Libraries.materialDesign)

    implementation(project(":platform:domain"))
    implementation(project(":platform:logger"))
    implementation(project(":platform:navigator"))
    implementation(project(":platform:state-machine"))
    implementation(project(":platform:shared-assets"))
    implementation(project(":platform:shared-utilities"))

    unitTest { forEachDependency { testImplementation(it) } }
    testImplementation(project(":platform:coroutines-testutils"))
    testImplementation(Libraries.turbine)
}