package io.dotanuki.norris.gradle.modules.models

import org.gradle.api.JavaVersion
import org.gradle.api.Project
import java.io.File
import java.util.Properties

data class PlatformDefinitions(
    val androidMinSdk: Int,
    val androidTargetSdk: Int,
    val androidBuildToolsVersion: String,
    val javaCompatibilityVersion: JavaVersion
) {

    companion object {
        fun from(project: Project): PlatformDefinitions {
            val propertiesFile = File("${project.rootProject.rootDir}/platform.properties")
            val properties = Properties().apply { load(propertiesFile.inputStream()) }

            val androidMinSdk = properties.extract("android.sdk.min").toInt()
            val androidTargetSdk = properties.extract("android.sdk.target").toInt()
            val buildToolsVersion = properties.extract("android.buildtools.version")

            val javaCompatibilityVersion = when (properties.extract("java.compatibility.version").toInt()) {
                8 -> JavaVersion.VERSION_1_8
                11 -> JavaVersion.VERSION_11
                else -> error("Compatible Java levels are : $COMPATIBLE_JAVA_BYTECODE_LEVELS")
            }

            return PlatformDefinitions(androidMinSdk, androidTargetSdk, buildToolsVersion, javaCompatibilityVersion)
        }

        private fun Properties.extract(key: String): String = getProperty(key) ?: error("Missing $key on properties")

        private val COMPATIBLE_JAVA_BYTECODE_LEVELS = listOf(8, 11)
    }
}
