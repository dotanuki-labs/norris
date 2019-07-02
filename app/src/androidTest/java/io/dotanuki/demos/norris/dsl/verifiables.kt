package io.dotanuki.demos.norris.dsl

import io.dotanuki.demos.norris.R

enum class Visibility {
    DISPLAYED, HIDDEN
}

enum class FactsContent {
    PRESENT, ABSENT
}

enum class ErrorImage(val resource: Int) {
    IMAGE_BUG_FOUND(R.drawable.img_bug_found)
}

enum class ErrorMessage(val resource: Int) {
    MESSAGE_BUG_FOUND(R.string.error_bug_found)
}