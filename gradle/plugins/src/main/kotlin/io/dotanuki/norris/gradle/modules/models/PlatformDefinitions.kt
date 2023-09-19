package io.dotanuki.norris.gradle.modules.models

import org.gradle.api.JavaVersion
import org.gradle.api.Project
import org.gradle.jvm.toolchain.JavaLanguageVersion
import org.gradle.jvm.toolchain.JvmVendorSpec
import java.io.File
import java.util.Properties

data class PlatformDefinitions(
    val androidMinSdk: Int,
    val androidTargetSdk: Int,
    val androidCompileSdk: Int,
    val androidBuildToolsVersion: String,
    val javaCompatibilityVersion: JavaVersion,
    val targetJdkVersion: JavaLanguageVersion,
    val targetJdkVendor: JvmVendorSpec = JvmVendorSpec.AZUL
) {

    companion object {
        fun from(project: Project): PlatformDefinitions {
            val propertiesFile = File("${project.rootProject.rootDir}/platform.properties")
            val properties = Properties().apply { load(propertiesFile.inputStream()) }

            val androidMinSdk = properties.extract("android.sdk.min").toInt()
            val androidTargetSdk = properties.extract("android.sdk.target").toInt()
            val androidCompileSdk = properties.extract("android.sdk.compile").toInt()
            val buildToolsVersion = properties.extract("android.buildtools.version")

            val javaBytecodeLevel = when (properties.extract("java.bytecode.level").toInt()) {
                8 -> JavaVersion.VERSION_1_8
                11 -> JavaVersion.VERSION_11
                17 -> JavaVersion.VERSION_17
                else -> error("Compatible Java levels are : $COMPATIBLE_JAVA_BYTECODE_LEVELS")
            }

            val javaToolchainVersion = properties.extract("java.toolchain.version").toInt()
            val targetJdk = JavaLanguageVersion.of(javaToolchainVersion)

            return PlatformDefinitions(
                androidMinSdk,
                androidTargetSdk,
                androidCompileSdk,
                buildToolsVersion,
                javaBytecodeLevel,
                targetJdk
            )
        }

        private fun Properties.extract(key: String): String = getProperty(key) ?: error("Missing $key on properties")

        private val COMPATIBLE_JAVA_BYTECODE_LEVELS = listOf(8, 11, 17)
    }
}
