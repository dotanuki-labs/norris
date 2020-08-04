import dependencies.UnitTestDependencies.Companion.unitTest
import modules.LibraryModule
import modules.LibraryType

val module = LibraryModule(rootDir, LibraryType.Kotlin)

apply(from = module.script())

plugins {
    id(BuildPlugins.Ids.kotlinJVM)
}

dependencies {
    implementation(Libraries.kotlinStdlib)
    implementation(Libraries.coroutinesCore)

    unitTest {
        forEachDependency { testImplementation(it) }
    }

    testImplementation(project(":coroutines-testutils"))
}