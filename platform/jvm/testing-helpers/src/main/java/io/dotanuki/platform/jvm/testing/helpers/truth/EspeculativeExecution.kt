package io.dotanuki.platform.jvm.testing.helpers.truth

import com.google.common.truth.FailureMetadata
import com.google.common.truth.Subject

class EspeculativeExecution(
    private val execution: () -> Unit
) : Subject.Factory<ExpectingErrorSubject, Throwable> {
    override fun createSubject(
        metadata: FailureMetadata?,
        actual: Throwable?
    ): ExpectingErrorSubject = ExpectingErrorSubject(metadata, actual, execution)
}
