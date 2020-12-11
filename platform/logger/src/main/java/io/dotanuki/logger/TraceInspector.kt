package io.dotanuki.logger

object TraceInspector {

    private const val grandFatherIndex = 2

    fun findClassName(): String {
        val stackTraceElement = Throwable().stackTrace[grandFatherIndex]
        return stackTraceElement.className
            .split(".").last()
            .split("$").first()
    }
}
