import dependencies.UnitTestDependencies.Companion.unitTest
import modules.LibraryModule
import modules.LibraryType
import modules.ModuleNames

val module = LibraryModule(rootDir, LibraryType.Kotlin)

apply(from = module.script())

plugins {
    id(BuildPlugins.Ids.kotlinJVM)
}

dependencies {
    implementation(project(ModuleNames.Logger))
    implementation(Libraries.coroutinesCore)

    unitTest {
        forEachDependency { testImplementation(it) }
    }

    testImplementation(Libraries.coroutinesTest)
    testImplementation(Libraries.coroutinesDebug)
}