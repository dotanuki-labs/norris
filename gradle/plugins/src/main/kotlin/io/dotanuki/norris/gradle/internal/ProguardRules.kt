package io.dotanuki.norris.gradle.internal

import java.io.File

class ProguardRules(private val pathToFiles: String) {

    val extras by lazy {
        File(pathToFiles).listFiles().toList().toTypedArray()
    }

    val androidDefault by lazy {
        "proguard-android-optimize.txt"
    }
}
