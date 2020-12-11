package io.dotanuki.logger

object MutedLogger : Logger {

    override fun v(message: String) = Unit

    override fun d(message: String) = Unit

    override fun i(message: String) = Unit

    override fun w(message: String) = Unit

    override fun e(message: String) = Unit
}
