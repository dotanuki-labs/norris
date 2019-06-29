import dependencies.UnitTestDependencies.Companion.unitTest
import modules.LibraryModule
import modules.LibraryType
import modules.ModuleNames

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

    implementation(project(ModuleNames.Domain))
    implementation(project(ModuleNames.Features.Architecture))
    implementation(project(ModuleNames.Features.SharedAssets))
    implementation(project(ModuleNames.Features.SharedUtilities))

    unitTest { forEachDependency { testImplementation(it) } }
    testImplementation(project(ModuleNames.CoroutinesTestUtils))
}