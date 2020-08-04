package configs

import java.io.File

class ProguardConfig(private val pathToFiles: String) {

    val customRules by lazy {
        File(pathToFiles).listFiles().toList().toTypedArray()
    }

    val androidRules by lazy {
        "proguard-android-optimize.txt"
    }
}