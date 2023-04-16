package io.dotanuki.platform.jvm.core.networking

import io.dotanuki.platform.jvm.core.networking.transformers.AggregatedErrorTransformer

suspend fun <T> managedExecution(target: suspend () -> T): T =
    try {
        target.invoke()
    } catch (incoming: Throwable) {
        throw AggregatedErrorTransformer.transform(incoming)
    }
