package io.dotanuki.platform.jvm.core.rest.util

fun Any.loadFile(path: String) =
    this.javaClass
        .classLoader
        .getResourceAsStream(path)
        .bufferedReader()
        .use { it.readText() }
