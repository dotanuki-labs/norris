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
    implementation(project(ModuleNames.Domain))
    implementation(Libraries.coroutinesCore)
    testImplementation(project(ModuleNames.CoroutinesTestUtils))

    unitTest {
        forEachDependency { testImplementation(it) }
    }
}