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
    implementation(Libraries.coroutinesTest)
    implementation(Libraries.coroutinesDebug)
    implementation(Libraries.jUnit)
}