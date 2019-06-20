package modules

import modules.LibraryType.Android
import modules.LibraryType.Kotlin
import java.io.File

class LibraryModule(
    private val rootDir: File,
    private val type: LibraryType
) {

    fun script() = "$rootDir/buildSrc/shared/${target()}"

    private fun target() = when (type) {
        Kotlin -> kotlinBuildLogic
        Android -> androidBuildLogic
    }

    private companion object {
        const val androidBuildLogic = "android-module.gradle"
        const val kotlinBuildLogic = "kotlin-module.gradle"
    }
}