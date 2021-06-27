package io.dotanuki.testing.app

import android.util.Log
import io.dotanuki.logger.Logger
import io.dotanuki.logger.TraceInspector

internal object LogcatLogger : Logger {

    override fun v(message: String) {
        Log.v(TraceInspector.findClassName(), message)
    }

    override fun d(message: String) {
        Log.d(TraceInspector.findClassName(), message)
    }

    override fun i(message: String) {
        Log.i(TraceInspector.findClassName(), message)
    }

    override fun w(message: String) {
        Log.w(TraceInspector.findClassName(), message)
    }

    override fun e(message: String) {
        Log.e(TraceInspector.findClassName(), message)
    }
}
