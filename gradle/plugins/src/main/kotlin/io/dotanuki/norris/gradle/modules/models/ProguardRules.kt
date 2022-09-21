package io.dotanuki.norris.gradle.modules.models

import java.io.File

internal class ProguardRules(private val pathToFiles: String) {

    val extras by lazy {
        File(pathToFiles).listFiles().toList().toTypedArray()
    }

    val androidDefault by lazy {
        "proguard-android-optimize.txt"
    }
}
