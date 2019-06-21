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
    implementation(Libraries.kotlinStdlib)

    implementation(Libraries.okhttp)
    implementation(Libraries.okhttpLogger)
    implementation(Libraries.retrofit)
    implementation(Libraries.retrofitKotlinSerialization)
    implementation(Libraries.coroutinesCore)

    implementation(Libraries.kodein)

    implementation(project(ModuleNames.Logger))
    implementation(project(ModuleNames.Domain))

    unitTest {
        forEachDependency { testImplementation(it) }
    }
}