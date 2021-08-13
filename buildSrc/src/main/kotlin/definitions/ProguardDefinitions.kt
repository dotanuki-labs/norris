package definitions

import java.io.File

class ProguardDefinitions(private val pathToFiles: String) {

    val customRules by lazy {
        File(pathToFiles).listFiles().toList().toTypedArray()
    }

    val androidRules by lazy {
        "proguard-android-optimize.txt"
    }
}
