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
    implementation(Libraries.appCompat)
    implementation(Libraries.lifecycleExtensions)
    implementation(Libraries.lifecycleViewModel)

    implementation(project(ModuleNames.Features.Architecture))

}