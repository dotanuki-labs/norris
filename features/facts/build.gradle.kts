import modules.LibraryModule
import modules.LibraryType
import modules.ModuleNames

val module = LibraryModule(rootDir, LibraryType.Android)

apply(from = module.script())

plugins {
    id(BuildPlugins.Ids.androidLibrary)
}

dependencies {
    implementation(project(ModuleNames.Logger))
    implementation(project(ModuleNames.Domain))
    implementation(project(ModuleNames.Features.SharedAssets))

    implementation(Libraries.appCompat)
}