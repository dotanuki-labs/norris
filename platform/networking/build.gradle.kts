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
    implementation(Libraries.kotlinSerialization)

    implementation(Libraries.okhttp)
    implementation(Libraries.okhttpLogger)
    implementation(Libraries.retrofit)
    implementation(Libraries.retrofitKotlinSerialization)
    implementation(Libraries.coroutinesCore)

    implementation(Libraries.kodein)

    implementation(project(":platform:logger"))
    implementation(project(":platform:domain"))

    unitTest {
        forEachDependency { testImplementation(it) }
    }

    testImplementation(project(":platform:coroutines-testutils"))
}