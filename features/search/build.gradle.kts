import dependencies.UnitTestDependencies.Companion.unitTest
import modules.LibraryModule
import modules.LibraryType

val module = LibraryModule(rootDir, LibraryType.Android)

apply(from = module.script())

plugins {
    id(BuildPlugins.Ids.androidLibrary)
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
}