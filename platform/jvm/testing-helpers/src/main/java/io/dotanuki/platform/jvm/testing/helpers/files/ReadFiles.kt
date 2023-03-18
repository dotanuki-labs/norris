package io.dotanuki.platform.jvm.testing.helpers.files

fun Any.loadFile(path: String) =
    this.javaClass
        .classLoader
        .getResourceAsStream(path)
        .bufferedReader()
        .use { it.readText() }
