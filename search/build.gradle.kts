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

    implementation(project(":domain"))
    implementation(project(":logger"))
    implementation(project(":navigator"))
    implementation(project(":unidirectional-dataflow"))
    implementation(project(":shared-assets"))
    implementation(project(":shared-utilities"))

    unitTest { forEachDependency { testImplementation(it) } }
    testImplementation(project(":coroutines-testutils"))
}