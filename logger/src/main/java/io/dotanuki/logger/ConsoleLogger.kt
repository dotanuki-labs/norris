package io.dotanuki.logger

object ConsoleLogger : Logger {

    override fun v(message: String) {
        println("VERBOSE/${TraceInspector.findClassName()}:$message")
    }

    override fun d(message: String) {
        println("DEBUG/${TraceInspector.findClassName()}:$message")
    }

    override fun i(message: String) {
        println("INFO/${TraceInspector.findClassName()}:$message")
    }

    override fun w(message: String) {
        println("WARNING/${TraceInspector.findClassName()}:$message")
    }

    override fun e(message: String) {
        System.err.println("ERROR/${TraceInspector.findClassName()}:$message")
    }

}